package ch.ehi.sqlgen;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

public class SqlReader {
    private SqlReader() {}
    public static String readSqlStmt(java.io.PushbackReader reader,Map<String,String> params)
    		throws IOException {
        if(params==null) {
            params=new HashMap<String,String>();
        }
    	StringBuffer stmt=new StringBuffer();
    	int c=reader.read();
    	while(c!=-1){
    		if(c=='-'){
    			c=reader.read();
    			if(c==-1){
    				stmt.append('-');
    				break;
    			}else if(c=='-'){
    				c=reader.read();
    				readCmt(c, reader);
    			}else{
    				stmt.append('-');
    				stmt.append((char)c);
    			}
    		}else if(c=='\''){ 
    			readQuotedString(c, reader, stmt,'\'');
            }else if(c=='\"'){ 
                readQuotedString(c, reader, stmt,'\"');
            }else if(c=='$'){
    			// ${param}
                readParam(c, reader, stmt,params);
    		}else if(c==';'){
    			// ignore ';'; not part of sql statement
    			// skip end of line
    			c=reader.read();
    			if(c=='\n'){
    				c=reader.read();
    				if(c!=-1 && c!='\r'){
    					reader.unread(c);
    				}
    			}else if(c=='\r'){
    				c=reader.read();
    				if(c!=-1 && c!='\n'){
    					reader.unread(c);
    				}
    			}else{
    				if(c!=-1){
    					reader.unread(c);
    					
    				}
    			}
    			break;
    		}else if(c=='\n'){
    			c=reader.read();
    			if(c!=-1 && c!='\r'){
    				reader.unread(c);
    			}
    			if(stmt.length()>0) {
                    stmt.append(' ');
    			}
    		}else if(c=='\r'){
    			c=reader.read();
    			if(c!=-1 && c!='\n'){
    				reader.unread(c);
    			}
                if(stmt.length()>0) {
                    stmt.append(' ');
                }
    		}else{
    			stmt.append((char)c);
    		}
    		c=reader.read();
    	}
    	String ret=stmt.toString().trim();
    	if(ret.length()==0){
    		return null;
    	}
    	return ret;
    }
    private static void readQuotedString(int c, java.io.PushbackReader reader, StringBuffer stmt,char quote) throws IOException {
        stmt.append((char)c);
        while(true){
        	c=reader.read();
        	if(c==-1){
        		break;
        	}else if(c==quote){ 
        		c=reader.read();
        		if(c==-1){
        			// eof
        			break;
        		}else if(c==quote){
        			stmt.append(quote);
        			stmt.append(quote);
        		}else{
        			reader.unread(c);
        			break;
        		}
        	}else{
        		stmt.append((char)c);
        	}
        }
        stmt.append(quote);
        return;
    }
    private static void readParam(int c, java.io.PushbackReader reader, StringBuffer stmt,Map<String,String> params) throws IOException {
        c=reader.read();
        StringBuffer paramName=new StringBuffer();
        if(c=='$') {
            stmt.append((char)'$');
            return;
        }else if(c!='{') {
            stmt.append((char)'$');
            reader.unread(c);
            return;
        }
        while(true){
            c=reader.read();
            if(c==-1){
                throw new IOException("unexpected termination of parameter");
            }else if(c=='}'){ 
                break;
            }else{
                paramName.append((char)c);
            }
        }
        String paramValue=params.get(paramName.toString());
        if(paramValue!=null) {
            stmt.append(paramValue);
        }
        return;
    }
    private static void readCmt(int c, java.io.PushbackReader reader) throws IOException {
        while(c!=-1){
        	if(c=='\n'){
        		c=reader.read();
        		if(c!=-1 && c!='\r'){
        			reader.unread(c);
        		}
        		break;
        	}else if(c=='\r'){
        		c=reader.read();
        		if(c!=-1 && c!='\n'){
        			reader.unread(c);
        		}
        		break;
        	}
        	c=reader.read();
        }
        return;
    }

}
