run

Function getCurDir()
    '�J�����g�f�B���N�g���擾
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

    '�t�@�C���p�X�w��
    curDir = getCurDir()
    inputFilePath = curDir & "\complete.tsv"
    outputFilePath = curDir & "\complete.txt"

    t = Timer
    res = conv(inputFilePath, outputFilePath)
    logPrint Timer - t & " �b"

End Sub

'https://daitaideit.com/vba-read-output-utf8-no/#mokuzi4
Function conv(inputFilePath, outputFilePath)
    
    Dim inputStream
    '-----------------------------------------------------------
    '�e�L�X�g�t�@�C����ǂݍ���
    With CreateObject("ADODB.Stream")
        .Charset = "SJIS"
        .Open
        .LoadFromFile inputFilePath '�t�@�C���ɐڑ�
        inputStream = .ReadText '�f�[�^��ǂݍ���
        .Close
    End With
    '-----------------------------------------------------------
    
    '-----------------------------------------------------------
    '���s���ɕ���
    line1 = Split(inputStream, vbCrLf)
    rowLen = UBound(line1) - 1 '�z���0�n�܂�̂���-1
    logPrint (rowLen)
    '-----------------------------------------------------------
    
    '-----------------------------------------------------------
    '��̔z����쐬(�s���͓���)
    ReDim line2(rowLen)
    colNum = 100;
    For i = 0 To rowLen
        line2(i) = ""
        columns = Split(line1(i), vbTab) '�^�u��؂�ŕ���
        For j = 0 to (colNum-1)
            line2(i) = line2(i) & columns(j)
        Next
    Next
    
    '-----------------------------------------------------------
    'BOM�Ȃ�UTF-8�ŏo�͂���
    With CreateObject("ADODB.Stream")
        .Charset = "UTF-8"
        .Open
        '�f�[�^����������
        For i = 0 To rowLen
            .WriteText line2(i), 1
        Next
        'BOM���폜����
        .Position = 0
        .Type = 1
        .Position = 3
        inputStream = .Read
        .Close
        .Open
        .Write inputStream
        '�ۑ�����t�@�C�����w��
        .SaveToFile outputFilePath, 2
        .Close
    End With
    '-----------------------------------------------------------
    
End Function

