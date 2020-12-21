$(function () {
    $('#cfg-list').datagrid({
        fitColumns: true,
        rownumbers: true,
        multipleSelect: true,
        collapsible: false,
        pagination: false,//分页栏
        singleSelect: false,//单选true
        url: '/draw/listCfg',
        method: 'get',
        datatype: 'json',
        columns: [[
            {field: 'id', title: '编号', width: 10, hidden: false, checkbox: true},
            {field: 'name', title: '供方名称', width: 30}
        ]],
        onLoadSuccess: function (data) {
            var rows = data.rows;
            if (rows) {
                for (var i = 0; i < rows.length; i++) {
                    if (rows[i].cfgStatus == 1) {
                        $('#cfg-list').datagrid('selectRow', i);
                    }
                }
            }
        }
    });

    initCombobox();

})

function save() {
    var rows = $('#cfg-list').datagrid('getChecked');
    if (rows) {
        var ids = '';
        for (var i = 0; i < rows.length; i++) {
            ids = ids + rows[i].id + ',';
        }
        ids = ids.substr(0, ids.length - 1);
        $.post('/draw/updateCfg', {ids: ids}, function (result) {
            if (result) {
                $.messager.show({
                    title: '消息',
                    msg: '更新成功！',
                    timeout: 5000,
                    showType: 'slide'
                });
            }
        }, 'json');
    }
    ;
};


function initCombobox() {
    $('#subject').combobox({
        url: '/subject/listAll',
        method: 'get',
        valueField: 'id',
        textField: 'name',
        onSelect: function (record) {
            $('#cfg-list').datagrid('load', {
                'subjectId': record.id
            });
        }
    });
}