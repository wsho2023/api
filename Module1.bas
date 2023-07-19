Attribute VB_Name = "Module1"
Sub run()
    Set dataSheet = ThisWorkbook.Worksheets("daicho")
    With dataSheet
        maxRow = .Cells(.Rows.Count, 1).End(xlUp).Row
        If (maxRow > 1) Then
            colIdx = 8
            Dim colls As New Collection
            
            matching = False
            For rowIdx = 2 To maxRow
                Code = .Cells(rowIdx, colIdx)
                Name = .Cells(rowIdx, (colIdx + 1))
                matching = False
                For Each cl In colls
                    If (cl.Code = Code) Then
                        matching = True
                        Exit For
                    End If
                Next
                If (matching = False) Then
                    Set clNew = New ClassMstInfo
                    clNew.Code = Code
                    clNew.Name = Name
                    clNew.cnt = 0
                    colls.add clNew
                End If
            Next rowIdx
            For rowIdx = 1 To maxRow
                colIdx = 8
                Code = .Cells(rowIdx, colIdx)
                Name = .Cells(rowIdx, (colIdx + 1))
                For Each cl In colls
                    If (cl.Code = Code) Then
                        cl.cnt = cl.cnt + 1
                        Exit For
                    End If
                Next
            Next rowIdx
            mailBody = "åèêî: " & (maxRow - 1)
            For Each cl In colls
                mailBody = mailBody & vbCr & cl.Name & "(" & cl.Code & "): " & cl.cnt
            Next
        Else
            mailBody = "åèêî: 0"
        End If
    End With
    
    Debug.Print mailBody

End Sub
