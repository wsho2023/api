run

Function getCurDir()
    'カレントディレクトリ取得
    Dim objShell
    Set objShell = CreateObject("WScript.Shell")
    getCurDir = objShell.CurrentDirectory
    'getCurDir = ThisWorkbook.Path & "\"
    Set objShell = Nothing
End Function

Function logPrint(msg)
    WScript.Echo msg
    'Debug.Print msg
End Function

Sub run()
    Dim curDir
    Dim inputFilePath
    Dim outputFilePath

    'ファイルパス指定
    curDir = getCurDir()
    inputFilePath = curDir & "\complete.tsv"
    outputFilePath = curDir & "\complete.txt"

    t = Timer
    res = conv(inputFilePath, outputFilePath)
    logPrint Timer - t & " 秒"

End Sub

'https://daitaideit.com/vba-read-output-utf8-no/#mokuzi4
Function conv(inputFilePath, outputFilePath)
    
    Dim inputStream
    '-----------------------------------------------------------
    'テキストファイルを読み込み
    With CreateObject("ADODB.Stream")
        .Charset = "SJIS"
        .Open
        .LoadFromFile inputFilePath 'ファイルに接続
        inputStream = .ReadText 'データを読み込む
        .Close
    End With
    '-----------------------------------------------------------
    
    '-----------------------------------------------------------
    '改行毎に分割
    line1 = Split(inputStream, vbCrLf)
    rowLen = UBound(line1) - 1 '配列は0始まりのため-1
    logPrint (rowLen)
    '-----------------------------------------------------------
    
    '-----------------------------------------------------------
    '空の配列を作成(行数は同じ)
    ReDim line2(rowLen)
    colNum = 100;
    For i = 0 To rowLen
        line2(i) = ""
        columns = Split(line1(i), vbTab) 'タブ区切りで分割
        For j = 0 to (colNum-1)
            line2(i) = line2(i) & columns(j)
        Next
    Next
    
    '-----------------------------------------------------------
    'BOMなしUTF-8で出力する
    With CreateObject("ADODB.Stream")
        .Charset = "UTF-8"
        .Open
        'データを書き込み
        For i = 0 To rowLen
            .WriteText line2(i), 1
        Next
        'BOMを削除する
        .Position = 0
        .Type = 1
        .Position = 3
        inputStream = .Read
        .Close
        .Open
        .Write inputStream
        '保存するファイルを指定
        .SaveToFile outputFilePath, 2
        .Close
    End With
    '-----------------------------------------------------------
    
End Function

