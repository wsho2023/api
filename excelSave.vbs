main

Sub main()
    Set oApp = CreateObject("Excel.Application")
    oApp.Visible = False    'Excel�͔�\���ɂ���
        Set WshArguments = Wscript.Arguments
        Set WshNamed = WshArguments.Named
        filePath = WshNamed("filePath")
        Wscript.Echo filePath
        Set wk = oApp.Workbooks.Open(filePath) '�t�@�C�����J��
        wk.Save
    oApp.Quit
End Sub

