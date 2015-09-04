
Sub run()
  ' create/open "data.mdb" in same directory
  dbCreateMDB dirname(DBEngine.WorkSpaces(0).Databases(0).Name) + "data.mdb"
  ' dbCreate DBEngine.Workspaces(0).Databases(0)
End Sub

Function dirname(path As String) As String
    Dim pos As Integer
    pos = instrr(path, "\")
    If pos = 0 Then
      dirname = ""
      Exit Function
    End If
    dirname = Left$(path, pos)
End Function

Function instrr(val As String, sr As String) As Integer
    Dim pos As Integer
    Dim lastpos As Integer
    
    pos = InStr(val, sr)
    If pos = 0 Then
      instrr = 0
      Exit Function
    End If
    Do
        lastpos = pos
        pos = InStr(lastpos + 1, val, sr)
    Loop Until pos = 0
    instrr = lastpos
End Function

Sub dbCreateMDB(mdbfilename As String)
  Dim workspace As workspace
  Dim database As database
  
  Set workspace = DBEngine.Workspaces(0)
  If existsDb(mdbfilename) Then
      Set database = workspace.OpenDatabase(mdbfilename, True)
  Else
      Set database = workspace.CreateDatabase(mdbfilename, dbLangGeneral)
  End If
  
  dbCreate database
 
  database.Close
  workspace.Close
End Sub
Sub dbCreate(database As database)
  dbCreateNLS database
  dbCreateP1 database
  dbCreateP2 database
  migrateDb database
End Sub
Function existsDb(mdbfilename As String) As Boolean
  On Error GoTo err
  Dim workspace As workspace
  Dim database As database
  
  Set workspace = DBEngine.WorkSpaces(0)
  Set database = workspace.OpenDatabase(mdbfilename, False)
  database.Close
  existsDb = True
  Exit Function
err:
  existsDb = False
End Function
Function existsTable(db As database, tablename As String) As Boolean
  On Error GoTo err
  Dim TableDef As TableDef
  
  Set TableDef = db.TableDefs(tablename)
  existsTable = True
  Exit Function
err:
  existsTable = False
End Function
Function existsRelation(db As database, relname As String) As Boolean
  On Error GoTo err
  Dim rel As Relation
  
  Set rel = db.Relations(relname)
  existsRelation = True
  Exit Function
err:
  existsRelation = False
End Function
Function existsIndex(TableDef As TableDef, indexname As String) As Boolean
  On Error GoTo err
  Dim Index As Index
  
  Set Index = TableDef.Indexes(indexname)
  existsIndex = True
  Exit Function
err:
  existsIndex = False
End Function
Function existsField(TableDef As TableDef, fieldname As String) As Boolean
  On Error GoTo err
  Dim Field As Field
  
  Set Field = TableDef.Fields(fieldname)
  existsField = True
  Exit Function
err:
  existsField = False
End Function
Public Sub addSequence(rskey As Recordset, tabname As String)
    rskey.Seek "=", tabname
    If rskey.NoMatch Then
        rskey.AddNew
        rskey.Fields("T_LastUniqueId") = 1
        rskey.Fields("T_LastChange") = Date+Time
        rskey.Fields("T_CreateDate") = Date+Time
        rskey.Fields("T_User") = "EisenhutInformatikAG"
        rskey.Fields("T_Key") = tabname
        rskey.Update
    End If
End Sub
Function getNewObjectKey(tablename As String, rskey As Recordset) As Long
   
   rskey.Seek "=", tablename
   Dim ret As Long
   ret = 0

   If rskey.NoMatch Then
      '
   Else
      ret = rskey.Fields("T_LastUniqueId")
      rskey.Edit
      rskey.Fields("T_LastUniqueId") = ret + 1
      rskey.Fields("T_LastChange") = Date+Time
      rskey.Update
   End If
   getNewObjectKey = ret
End Function
Public Sub dbAddEnumEle(rskey As Recordset, rsenum As Recordset, rsnls As Recordset, rsTrsl As Recordset, lang As String, ilicode As String, txt As String,seq as Integer)
  Dim nlsid As Long
  nlsid = getNewObjectKey(rsnls.Name, rskey)
  rsnls.AddNew
  rsnls.Fields("T_id") = nlsid
  rsnls.Fields("T_LastChange") = Date+Time
  rsnls.Fields("T_CreateDate") = Date+Time
  rsnls.Fields("T_User") = "EisenhutInformatikAG"
  rsnls.Update
  
  rsTrsl.AddNew
  rsTrsl.Fields("T_Id_Nls") = nlsid
  rsTrsl.Fields("T_LastChange") = Date+Time
  rsTrsl.Fields("T_CreateDate") = Date+Time
  rsTrsl.Fields("T_User") = "EisenhutInformatikAG"
  rsTrsl.Fields("Language") = lang
  rsTrsl.Fields("NlsText") = txt
  rsTrsl.Update
  
  rsenum.AddNew
  rsenum.Fields("T_Id") = getNewObjectKey(rsenum.Name, rskey)
  rsenum.Fields("T_LastChange") = Date+Time
  rsenum.Fields("T_CreateDate") = Date+Time
  rsenum.Fields("T_User") = "EisenhutInformatikAG"
  rsenum.Fields("T_Id_Name") = nlsid
  rsenum.Fields("IliCode") = ilicode
  if seq<>0 then
	  rsenum.Fields("T_Sequence") = seq
  end if
  rsenum.Update

End Sub
Public Sub dbCreateNLS(database As database)
    Dim TableDef As TableDef
    Dim QueryDef As QueryDef
    Dim Index As Index
    Dim Field As Field
    Dim Relation As Relation
    
    Dim createTable As Boolean
    createTable = not existsTable(database, "T_Key_Object")
    If createTable Then
        Set TableDef = database.CreateTableDef("T_Key_Object")
    Else
        Set TableDef = database.TableDefs("T_Key_Object")
    End If
    If Not existsField(TableDef, "T_User") Then
        Set Field = TableDef.CreateField("T_User", dbText, 40)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    
    If Not existsField(TableDef, "T_LastUniqueId") Then
        Set Field = TableDef.CreateField("T_LastUniqueId", dbLong)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_LastChange") Then
        Set Field = TableDef.CreateField("T_LastChange", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_Key") Then
        Set Field = TableDef.CreateField("T_Key", dbText, 40)
        TableDef.Fields.Append Field
        Set Field = TableDef.CreateField("T_CreateDate", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If createTable Then
        database.TableDefs.Append TableDef
    End If
    
    '  CREATE INDEX "PrimaryKey"
    Set TableDef = database.TableDefs("T_Key_Object")
    If Not existsIndex(TableDef, "PrimaryKey") Then
        Set Index = TableDef.CreateIndex("PrimaryKey")
        Set Field = Index.CreateField("T_Key")
        Index.Fields.Append Field
        Index.Primary = True
        TableDef.Indexes.Append Index
    End If
    
    Dim rskey As Recordset
    Set rskey = database.OpenRecordset("T_Key_Object", dbOpenTable)
    rskey.Index = "PrimaryKey"
    
    '  CREATE TABLE "T_MAP_NLS"
    createTable = not existsTable(database, "T_MAP_NLS")
    If createTable Then
        Set TableDef = database.CreateTableDef("T_MAP_NLS")
    Else
        Set TableDef = database.TableDefs("T_MAP_NLS")
    End If
    If Not existsField(TableDef, "T_Type_Owner") Then
        Set Field = TableDef.CreateField("T_Type_Owner", dbText, 30)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_Id_Owner") Then
        Set Field = TableDef.CreateField("T_Id_Owner", dbLong)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_Id_Nls") Then
        Set Field = TableDef.CreateField("T_Id_Nls", dbLong)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_User") Then
        Set Field = TableDef.CreateField("T_User", dbText, 40)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_Attribute_Owner") Then
        Set Field = TableDef.CreateField("T_Attribute_Owner", dbText, 30)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_CreateDate") Then
        Set Field = TableDef.CreateField("T_CreateDate", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_LastChange") Then
        Set Field = TableDef.CreateField("T_LastChange", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If createTable Then
        database.TableDefs.Append TableDef
    End If
    
    '  CREATE INDEX "PrimaryKey"
    Set TableDef = database.TableDefs("T_MAP_NLS")
    If Not existsIndex(TableDef, "PrimaryKey") Then
        Set Index = TableDef.CreateIndex("PrimaryKey")
        Set Field = Index.CreateField("T_Type_Owner")
        Index.Fields.Append Field
        Set Field = Index.CreateField("T_Id_Owner")
        Index.Fields.Append Field
        Set Field = Index.CreateField("T_Attribute_Owner")
        Index.Fields.Append Field
        Index.Primary = True
        TableDef.Indexes.Append Index
    End If
    
    '  CREATE TABLE "T_NLS"
    createTable = not existsTable(database, "T_NLS")
    If createTable Then
        Set TableDef = database.CreateTableDef("T_NLS")
    Else
        Set TableDef = database.TableDefs("T_NLS")
    End If
    If Not existsField(TableDef, "T_User") Then
        Set Field = TableDef.CreateField("T_User", dbText, 40)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_Id") Then
        Set Field = TableDef.CreateField("T_Id", dbLong)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_LastChange") Then
        Set Field = TableDef.CreateField("T_LastChange", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "Symbol") Then
        Set Field = TableDef.CreateField("Symbol", dbText, 61)
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_CreateDate") Then
        Set Field = TableDef.CreateField("T_CreateDate", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If createTable Then
        database.TableDefs.Append TableDef
    End If
        
    addSequence rskey, TableDef.Name
    
    '  CREATE INDEX "PrimaryKey"
    Set TableDef = database.TableDefs("T_NLS")
    If Not existsIndex(TableDef, "PrimaryKey") Then
        Set Index = TableDef.CreateIndex("PrimaryKey")
        Set Field = Index.CreateField("T_Id")
        Index.Fields.Append Field
        Index.Primary = True
        TableDef.Indexes.Append Index
    End If
    
    '  CREATE TABLE "T_Translation"
    createTable = not existsTable(database, "T_Translation")
    If createTable Then
        Set TableDef = database.CreateTableDef("T_Translation")
    Else
        Set TableDef = database.TableDefs("T_Translation")
    End If
    If Not existsField(TableDef, "Language") Then
        Set Field = TableDef.CreateField("Language", dbText, 2)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_Id_Nls") Then
        Set Field = TableDef.CreateField("T_Id_Nls", dbLong)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "Country") Then
        Set Field = TableDef.CreateField("Country", dbText, 2)
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "NlsText") Then
        Set Field = TableDef.CreateField("NlsText", dbMemo, 1024)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_User") Then
        Set Field = TableDef.CreateField("T_User", dbText, 40)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_LastChange") Then
        Set Field = TableDef.CreateField("T_LastChange", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If Not existsField(TableDef, "T_CreateDate") Then
        Set Field = TableDef.CreateField("T_CreateDate", dbDate)
        Field.Required = True
        TableDef.Fields.Append Field
    End If
    If createTable Then
        database.TableDefs.Append TableDef
    End If
    
    '  CREATE INDEX "PrimaryKey"
    Set TableDef = database.TableDefs("T_Translation")
    If Not existsIndex(TableDef, "PrimaryKey") Then
        Set Index = TableDef.CreateIndex("PrimaryKey")
        Set Field = Index.CreateField("Language")
        Index.Fields.Append Field
        Set Field = Index.CreateField("T_Id_Nls")
        Index.Fields.Append Field
        Index.Primary = True
        TableDef.Indexes.Append Index
    End If
    
    '  CREATE INDEX "T_IX_UniqueEntry"
    Set TableDef = database.TableDefs("T_Translation")
    If Not existsIndex(TableDef, "T_IX_UniqueEntry") Then
        Set Index = TableDef.CreateIndex("T_IX_UniqueEntry")
        Set Field = Index.CreateField("Language")
        Index.Fields.Append Field
        Set Field = Index.CreateField("T_Id_Nls")
        Index.Fields.Append Field
        Set Field = Index.CreateField("Country")
        Index.Fields.Append Field
        Index.Unique = True
        TableDef.Indexes.Append Index
    End If
    
    '  CREATE RELATIONSHIP "strings"
    If Not existsRelation(database, "strings") Then
        Set Relation = database.CreateRelation("strings", "T_NLS", "T_MAP_NLS")
        Set Field = Relation.CreateField("T_Id")
        Field.ForeignName = "T_Id_Nls"
        Relation.Fields.Append Field
        database.Relations.Append Relation
    End If
    
    '  CREATE RELATIONSHIP "translations"
    If Not existsRelation(database, "translations") Then
        Set Relation = database.CreateRelation("translations", "T_NLS", "T_Translation")
        Set Field = Relation.CreateField("T_Id")
        Field.ForeignName = "T_Id_Nls"
        Relation.Fields.Append Field
        Relation.Attributes = Relation.Attributes + dbRelationDeleteCascade
        database.Relations.Append Relation
    End If
End Sub
Sub migrateDb(database As database)

End Sub
