'cscript getLineCount.vbs
run

Sub run()
    filePath = getCurDir & "complete.txt"
    'filePath = "complete.txt"   '同フォルダにあるファイル
    Line = GetLineCount(filePath) - 1
    If Line > 1 Then
        logPrint(CStr(Line))
    Else
        logPrint("Data not found")
    End If
End Sub

Function getCurDir()
    'カレントディレクトリ取得
    Dim objShell
    Set objShell = CreateObject("WScript.Shell")
    getCurDir = objShell.CurrentDirectory & "\"
    'getCurDir = ThisWorkbook.Path & "\"
    Set objShell = Nothing
End Function

Function logPrint(msg)
    WScript.Echo msg
    'Debug.Print msg
End Function

Function GetLineCount(filePath)
    Dim fso
    Set fso = CreateObject("Scripting.FileSystemObject")
    
    'ファイルが存在しない場合は終了
    If (fso.FileExists(filePath) = False) Then
        GetLineCount = -1
        Exit Function
    End If
    '追加書き込みモードでテキストオープン
    With fso.OpenTextFile(filePath, 8)
        GetLineCount = .Line
        .Close
    End With
    Set fso = Nothing
End Function
