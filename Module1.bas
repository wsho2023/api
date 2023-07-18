Attribute VB_Name = "Module1"
Sub run()
    Set dataSheet = ThisWorkbook.Worksheets("daicho")
    With dataSheet
        maxRow = .Cells(.Rows.Count, 1).End(xlUp).Row
        If (maxRow > 1) Then
            colIdx = 8
            Dim classMstList As Object
            Set classMstList = CreateObject("System.Collections.ArrayList")
            
            matching = False
            For rowIdx = 2 To maxRow
                Code = .Cells(rowIdx, colIdx)
                Name = .Cells(rowIdx, (colIdx + 1))
                matching = False
                For Each cm In classMstList
                    If (cm.Code = Code) Then
                        matching = True
                        Exit For
                    End If
                Next
                If (matching = False) Then
                    Set cmNew = New ClassMstInfo
                    cmNew.Code = Code
                    cmNew.Name = Name
                    cmNew.cnt = 0
                    classMstList.add cmNew
                End If
            Next rowIdx
            For rowIdx = 1 To maxRow
                colIdx = 8
                Code = .Cells(rowIdx, colIdx)
                Name = .Cells(rowIdx, (colIdx + 1))
                For Each cm In classMstList
                    If (cm.Code = Code) Then
                        cm.cnt = cm.cnt + 1
                        Exit For
                    End If
                Next
            Next rowIdx
            mailBody = "åèêî: " & (maxRow - 1)
            For Each cm In classMstList
                mailBody = mailBody & vbCr & cm.Name & "(" & cm.Code & "): " & cm.cnt
            Next
        Else
            mailBody = "åèêî: 0"
        End If
    End With
    
    Debug.Print mailBody

End Sub
