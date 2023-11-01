main

Sub main()
    Set oApp = CreateObject("Excel.Application")
    oApp.Visible = False    'Excelは非表示にする
        Set WshArguments = Wscript.Arguments
        Set WshNamed = WshArguments.Named
        filePath = WshNamed("filePath")
        Wscript.Echo filePath
        Set wk = oApp.Workbooks.Open(filePath) 'ファイルを開く
        wk.Save
    oApp.Quit
End Sub

