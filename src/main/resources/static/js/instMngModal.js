var cityName;
var cityData;
$(function () {
    //$('#instMngList').datagrid('enableCellEditing');
    $('#instMngList').datagrid({
        fitColumns: true,
        rownumbers: true,
        multipleSelect: true,
        collapsible: false,
        pagination: false,//分页栏
        singleSelect: false,//单选true
        url: '/inst/listAll',
        method: 'get',
        datatype: 'json',
        columns: [[
            {field: 'id', title: '编号', width: 10, checkbox:true},
            {field: 'name', title: '供方名称', width: 30}
            /*{
                field: 'state', title: '操作', width: 30, hidden:true,
                formatter: function (value, row, index) {
                    var delHtml = "<a title='删除' class='tx_red' οnclick='deleteRightGridRow(\"" + row.id + "\",event);' href='javascript:void(0);'>删除</a>";
                    return delHtml;
                }
            }*/
        ]],
        onLoadSuccess: function (data) {
            /*            var s = $("#instMngList").datagrid('getPanel');
                        var rows1 = s.find('tr.datagrid-row td[field!=state1]');
                        rows1.unbind('click').bind('click', function (e) {
                            return false;
                        });*/
        }
    });

    $(".tx_red").click(function () {
        console.log('stopPropagation1');
        event.stopPropagation();
        console.log('stopPropagation2');
    });

});

function deletee(val, row, index) {
    //return "<a href='#' onclik='change('+ row +')'>删除</a>"
    return "<button class='btn btn-primary del' οnclick=\"change(" + row.cmgId + "," + val + ")\">删除</button>";
}

function change(row, val) {
}

function deleteRightGridRow(rowId) {
    var datas = $('#instMngList').datagrid('getData');
    for (var i = 0; i < datas.rows.length; i++) {
        if (datas.rows[i].id == rowId) {//数据中的id和渲染时的id相等
            //通过传入的id值查询到对应的记录，在获取实际的Index,这样去删除,（直接传入渲染好的索引值会出现错误）
            var rowIndex = $('#instMngList').datagrid('getRowIndex', datas.rows[i]);
            $('#instMngList').datagrid('deleteRow', rowIndex);
            return;
        }
    }
};

function addProject() {
    form = $("<form></form>")
    form.attr('action', action)
    form.attr('method', 'post')
    input1 = $("<input type='hidden' name='input1' />")
    input1.attr('value', 'input1 value')
    input2 = $("<input type='text' name='textinput' value='text input' />")
    form.append(input1)
    form.append(input2)
    form.appendTo("body")
    form.css('display', 'none')
    form.submit()
};

function addInst() {

    $('#modal-addInst').window('open');
    $('#modal-addInst').window('setTitle','新增供方单位');
    $('#instName').textbox('setValue','');

    $('#select-city').combobox({
        url: '/inst/city/combobox',
        method: 'get',
        valueField: 'cityName',
        textField: 'cityName',
        multiple:true
    });


    $('#addInstForm').form({
        url: '/inst/add',
        onSubmit: function () {
            return $(this).form('enableValidation').form('validate');
        },
        success: function (data) {
            // 关闭模态框
            $('#modal-addInst').window('close');
            // 刷新列表
            $('#instMngList').datagrid('reload');
        }
    });
}

function editInst() {

    var rows = $('#instMngList').datagrid('getChecked');

    if (rows) {

        if (rows.length != 1) {
            //清选择一条数据进行修改
            $.messager.alert('提示','请选择一条数据进行修改！','warning');
            return;
        }
        $('#modal-addInst').window('open');
        $('#modal-addInst').window('setTitle','修改供方单位');

        $('#instName').textbox('setValue',rows[0].name);


        $('#select-city').combobox({
            url: '/inst/city/combobox',
            method: 'get',
            valueField: 'cityName',
            textField: 'cityName',
            multiple:true
        });
        //$('#instName').textbox('setValue',rows[0].name);
        $('#select-city').combobox('select',rows[0].city);

        // 修改供方
        $('#addInstForm').form({
            url: '/inst/update',
            onSubmit: function (param) {
                param.id = rows[0].id;
                return $(this).form('enableValidation').form('validate');
            },
            success: function (data) {
                // 关闭模态框
                $('#modal-addInst').window('close');
                // 刷新列表
                $('#instMngList').datagrid('reload');
            }
        });
    }
}

function delInst() {
    var rows = $('#instMngList').datagrid('getChecked');
    if (rows) {

        if (rows.length < 1) {
            //清选择一条数据进行删除
            $.messager.alert('提示','请选择需要删除的数据！','warning');
            return;
        }

        $.messager.confirm('Confirm', '确认删除供方单位？', function (r) {
            if (r) {

                var ids = [];
                for (var i = 0;i < rows.length; i++){
                    ids.push(rows[i].id);
                }
                var idsStr = ids.join();

                $.post('/inst/delete', {ids: idsStr}, function (result) {
                    if (result.code == 'success') {
                        $('#instMngList').datagrid('reload');	// reload the user data
                    } else {
                        $.messager.show({	// show error message
                            title: 'Error',
                            msg: '删除失败！'
                        });
                    }
                }, 'json');
            }
        });
    }
}

function editCity(){
    $('#modal-editCity').window('open');

    if(!cityName){
        $.ajax({
            url: '/inst/city/get',
            type: 'GET',
            success: function (data) {
                cityName = data.data;
                $('#cityName').textbox('setValue',cityName);
            }
        });
    }else{
        $('#cityName').textbox('setValue',cityName);
    }

    $('#editCityForm').form({
        url: '/inst/city/edit',

        success: function (data) {

            cityName = $('#cityName').textbox('getValue');
            // 关闭模态框
            $('#modal-editCity').window('close');
            // 刷新列表
            // $('#instMngList').datagrid('reload');
        }
    });
}





