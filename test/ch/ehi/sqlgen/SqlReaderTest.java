package ch.ehi.sqlgen;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;

import org.junit.Test;

public class SqlReaderTest {

	@Test
	public void testEmptyFile() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader(""));
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testSingleStmt() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("SELECT;"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("SELECT",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void test2Stmt1Line() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("SELECT;DROP;"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("SELECT",stmt1);
		String stmt2=SqlReader.readSqlStmt(reader,null);
		assertEquals("DROP",stmt2);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void test2StmtLf() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("SELECT;\nDROP;"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("SELECT",stmt1);
		String stmt2=SqlReader.readSqlStmt(reader,null);
		assertEquals("DROP",stmt2);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testMultipleNewlines() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("\n\nSELECT;\n\nDROP;\n\n"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("SELECT",stmt1);
		String stmt2=SqlReader.readSqlStmt(reader,null);
		assertEquals("DROP",stmt2);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testOnlyCmt() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("-- a comment"));
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testCmtStmt() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("-- a comment\nSELECT;"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("SELECT",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testCmtStmtCmt() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("-- a comment\nSELECT;-- comment\n-- more comment "));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("SELECT",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testQuote() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("'aaa'"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("'aaa'",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testEmptyQuote() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("''"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("''",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testEndQuote() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("'a'''"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("'a'''",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
	@Test
	public void testBeginQuote() throws IOException {
		PushbackReader reader=new PushbackReader(new StringReader("'''a'"));
		String stmt1=SqlReader.readSqlStmt(reader,null);
		assertEquals("'''a'",stmt1);
		String stmtEnd=SqlReader.readSqlStmt(reader,null);
		assertNull(stmtEnd);
	}
    @Test
    public void testParam() throws IOException {
        PushbackReader reader=new PushbackReader(new StringReader("c${aaa}c"));
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("aaa", "bbb");
        String stmt1=SqlReader.readSqlStmt(reader,params);
        assertEquals("cbbbc",stmt1);
        String stmtEnd=SqlReader.readSqlStmt(reader,params);
        assertNull(stmtEnd);
    }
    @Test
    public void testParamUndefined() throws IOException {
        PushbackReader reader=new PushbackReader(new StringReader("c${aaa}c"));
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("yyy", "bbb");
        String stmt1=SqlReader.readSqlStmt(reader,params);
        assertEquals("cc",stmt1);
        String stmtEnd=SqlReader.readSqlStmt(reader,params);
        assertNull(stmtEnd);
    }
    @Test
    public void testParamEscape() throws IOException {
        PushbackReader reader=new PushbackReader(new StringReader("c$${aaa}c"));
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("aaa", "bbb");
        String stmt1=SqlReader.readSqlStmt(reader,params);
        assertEquals("c${aaa}c",stmt1);
        String stmtEnd=SqlReader.readSqlStmt(reader,params);
        assertNull(stmtEnd);
    }
    @Test
    public void testParamUnclosed() throws IOException {
        PushbackReader reader=new PushbackReader(new StringReader("c${aaac"));
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("aaa", "bbb");
        try {
            String stmt1=SqlReader.readSqlStmt(reader,params);
            fail();
        }catch(IOException e) {
            
        }
    }
    @Test
    public void testParamInQuote() throws IOException {
        PushbackReader reader=new PushbackReader(new StringReader("'${aaa}'"));
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("aaa", "bbb");
        String stmt1=SqlReader.readSqlStmt(reader,params);
        assertEquals("'${aaa}'",stmt1);
        String stmtEnd=SqlReader.readSqlStmt(reader,params);
        assertNull(stmtEnd);
    }
    @Test
    public void testParamInDoubleQuote() throws IOException {
        PushbackReader reader=new PushbackReader(new StringReader("\"${aaa}\""));
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("aaa", "bbb");
        String stmt1=SqlReader.readSqlStmt(reader,params);
        assertEquals("\"${aaa}\"",stmt1);
        String stmtEnd=SqlReader.readSqlStmt(reader,params);
        assertNull(stmtEnd);
    }

}
