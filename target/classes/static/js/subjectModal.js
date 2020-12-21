function getSubjectList() {
    //开启模态框
    $('#modal-subject').window('open');
    $('#subjectTab').datagrid({
        view: detailview,
        url: '/subject/listAll',
        singleSelect: "true",
        fitColumns: "true",
        striped: true,
        checkOnSelect: 'true',
        showHeader:false,
        //autoRowHeight:true,
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: addSubject
        }, {
            text: '修改',
            iconCls: 'icon-edit',
            handler: editSubject
        }, {
            text: '删除',
            iconCls: 'icon-remove',
            handler: delSubject
        }],
        method: 'get',
        columns: [[
            {field: 'id', title: '主键', width: 200, hidden: true},
            {field: 'name', title: '科目名称', width: '98%'}
        ]],
        detailFormatter: function (index, row) {
            return '<div style="padding:2px"><table id="ddv1-' + index + '" style="width: 100%;"></table></div>';
        },
        onExpandRow: function (index, row) {
            var subId = row.id;
            $('#ddv1-' + index).datagrid({
                url: '/subject/level?subId=' + subId,
                view: detailview,
                fitColumns: true,
                singleSelect: true,
                rownumbers: false,
                showHeader:false,
                autoRowHeight:true,
                loadMsg: '',
                //height: 'auto',
                method: 'get',
                columns: [[
                    {field: 'levelId', title: '主键', width: 100, hidden: true},
                    {field: 'levelName', title: '供方小类', width: '98%', align: 'left'}
                ]],
                detailFormatter: function (index2, row) {
                    return '<div style="padding:2px"><table id="ddv2-' + subId + '-' + index2 + '" style="width: 100%;"></table></div>';
                },
                onCollapseRow: function(index2, row2){
                    console.log("onCollapseRow>>>>>>>index=" + index2);
                    var ddv2 = $(this).datagrid('getRowDetail',index2).find('#ddv2-'+ subId + '-' + index2);
                    setTimeout(function () {
                        ddv2.datagrid('fixDetailRowHeight', index2);
                        $('#ddv1-' + index).datagrid('fixRowHeight', index2);
                        $('#ddv1-' + index).datagrid('fixDetailRowHeight', index2);
                        $('#subjectTab').datagrid('fixDetailRowHeight', index);
                    }, 0);
                },
                onExpandRow: function (index2, row2) {
                    console.log("onExpandRow2>>>>>>index1=" + index);
                    console.log("index2=" + index2);
                    var levelId = row2.levelId;
                    var gridIndex = index2;
                    //var ddv2 = $(this).datagrid('getRowDetail',index2).find('#ddv2-'+index2);
                    var ddv2 = $(this).datagrid('getRowDetail',index2).find('#ddv2-'+ subId + '-' + index2);
                    ddv2.datagrid({
                        url: '/subject/detail?subId='+ subId + '&levelId=' + levelId,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        showHeader:false,
                        //autoRowHeight:true,
                        loadMsg: '',
                        height: 'auto',
                        method: 'get',
                        columns: [[
                            {field: 'id', title: '供方主键', width: 100, hidden: true},
                            {
                                field: 'subId',
                                title: '科目主键',
                                width: '10%',
                                hidden: true,
                                formatter: function (value, row, index) {
                                    row.subId = subId;
                                    row.gridIndex = gridIndex;
                                    row.levelId = levelId;
                                    return "<span>" + subId + "</span>";
                                }
                            },
                            {field: 'name', title: '供方名称', width: '80%', align: 'left'},
                            {field: 'op', title: '操作', width: '20%', align: 'center', formatter: formatOper1}
                        ]],

                        onResize: function () {
                            ddv2.datagrid('fixDetailRowHeight', index2);
                            $('#subjectTab').datagrid('fixDetailRowHeight', index);
                        },
                        onLoadSuccess: function () {
                            setTimeout(function () {
                                ddv2.datagrid('fixDetailRowHeight', index2);
                                $('#ddv1-' + index).datagrid('fixRowHeight', index2);
                                $('#ddv1-' + index).datagrid('fixDetailRowHeight', index2);
                                $('#subjectTab').datagrid('fixDetailRowHeight', index);
                            }, 0);
                        }
                    });
                    ddv2.datagrid('fixDetailRowHeight', index2);
                    $('#subjectTab').datagrid('fixDetailRowHeight', index);
                },
                onResize: function () {
                    $('#subjectTab').datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess: function () {
                    setTimeout(function () {
                        $('#subjectTab').datagrid('fixDetailRowHeight', index);
                    }, 0);
                }
            });
            $('#subjectTab').datagrid('fixDetailRowHeight', index);
        }
    });
};

function formatOper1(val, row, index) {
/*    return '<a href="#" onclick="moveInst(' + row.subId + ',' + row.id + ',' + row.gridIndex + ')">移动</a>&nbsp;&nbsp;' +
        '<a href="#" onclick="removeInst(' + row.subId + ',' + row.id + ',' + row.gridIndex + ')">删除</a>';*/

      return  '<a href="#" onclick="removeInst(' + row.subId + ',' + row.id + ',' + row.gridIndex + ')">删除</a>';
}

/*移动供方到其他科目*/
function moveInst(subId, instId, gridIndex) {

}

/*删除科目下的某个供方*/
function removeInst(subId, instId, gridIndex) {
    $.messager.confirm('Confirm', '确认将此供方从科目中移除？', function (r) {
        if (r) {
            $.post('/subject/removeInst', {subId: subId, instId: instId}, function (result) {
                if (result.code == 'success') {
                    $('#ddv2-' + subId + '-' + gridIndex).datagrid('reload');
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

function addSubject() {


    // 设置title
    $('#modal-editSubject').window({'title': "新增科目"});
    // 清空表单
    $('#subjectName').textbox('setValue', '');

    $('#s-instName').textbox('setValue', '');
    $('#modal-editSubject').window('open');
    initLeft1();
    initRight1();

    $('#subjectForm').form({
        url: '/subject/add',
        onSubmit: function (param) {
            var datas = $('#rightList1').datagrid('getData');

            if (datas.rows.length == 0) {
                $.messager.alert('提示', '组内供方列表为空！', 'warning');
                return false;
            }

            var rows = JSON.stringify(datas.rows);
            rows = rows.replace(/\"/g, "'");
            param.list = JSON.stringify(rows);

            return $(this).form('enableValidation').form('validate');
        },
        success: function (result) {
            var result = eval('(' + result + ')');
            // 将新项目插入项目列表
            $('#subjectTab').datagrid('insertRow', {
                index: 0,
                row: result.data
            });
            // 关闭模态框
            $('#modal-editSubject').window('close');
        }
    });
}

function editSubject() {

    // 清空搜索框
    $('#s-instName').textbox('setValue', '');

    var rows = $('#subjectTab').datagrid('getChecked');
    if (rows) {

        if (rows.length != 1) {
            //选择一条数据进行修改
            $.messager.alert('提示', '请选择一条数据进行修改！', 'warning');
            return;
        }

        // 设置title
        $('#modal-editSubject').window({'title': "修改科目"});
        $('#modal-editSubject').window('open');
        initLeft1();
        initRight1(rows[0].id);

        $('#subjectName').textbox('setValue', rows[0].name);
        var id = rows[0].id;

        // 修改供方
        $('#subjectForm').form({
            url: '/subject/update',

            onSubmit: function (param) {
                var datas = $('#rightList1').datagrid('getData');
                if (datas.rows.length == 0) {
                    $.messager.alert('提示', '组内供方列表为空！', 'warning');
                    return false;
                }
                var rows = JSON.stringify(datas.rows);
                rows = rows.replace(/\"/g, "'");    //把json字符串中的双引号替换为 单引号
                param.list = JSON.stringify(rows);
                param.id = id;
                return $(this).form('enableValidation').form('validate');
            },
            success: function (result) {
                var result = eval('(' + result + ')');
                if (result.code == 'success') {
                    // 关闭模态框
                    $('#modal-editSubject').window('close');
                    //刷新列表
                    $('#subjectTab').datagrid('reload');	// reload the user data
                } else {
                    $.messager.show({	// show error message
                        title: 'Error',
                        msg: '删除失败！'
                    });
                }
            }
        });
    }
}

function delSubject() {
    var rows = $('#subjectTab').datagrid('getChecked');
    if (rows) {
        if (rows.length != 1) {
            //选择一条数据进行修改
            $.messager.alert('提示', '请选择一条数据进行删除！', 'warning');
            return;
        }
        $.messager.confirm('Confirm', '确认删除科目？', function (r) {
            if (r) {

                $.post('/subject/delete', {id: rows[0].id}, function (result) {
                    if (result.code == 'success') {
                        $('#subjectTab').datagrid('reload');	// reload the user data
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

function doSearch(value) {

    $('#leftList1').datagrid('load', {
        'name': value,
        'sp[name]': value
    });
}

function initLeft1() {
    $('#leftList1').datagrid({
        idField: "id",
        fitColumns: true,
        rownumbers: true,
        collapsible: false,
        pagination: false,//分页栏
        showHeader: true,//显示头部
        singleSelect: false,//单选
        url: '/inst/listAll',
        method: 'get',
        datatype: 'json',
        columns: [[
            {field: 'id', title: '主键', width: 30, checkbox: true},
            {field: 'name', title: '所有供方', width: 30}
        ]],
        onLoadSuccess: function (data) {
        },
        // 双击行事件
        onDblClickCell: function (index, field, value) {
        }
    });

    $('#leftList1').datagrid("unselectAll");
}

function initRight1(id) {
    var url = '';
    if (id) {
        url = '/subject/getCurrentInstList?id=' + id;
    } else {
        url = '/subject/getCurrentInstList';
    }

    $('#rightList1').datagrid({
        idField: "id",
        fitColumns: true,
        rownumbers: true,
        collapsible: false,
        pagination: false,//分页栏
        showHeader: true,//隐藏头部
        checkbox: true,
        method: 'get',
        url: url,
        datatype: 'json',
        columns: [[
            {field: 'id', title: 'id', width: 30, checkbox: true},
            {field: 'name', title: '组内供方', width: 30}
        ]],
        onLoadSuccess: function (data) {
            /* var s = $("#instMngList").datagrid('getPanel');
             var rows1 = s.find('tr.datagrid-row td[field!=state1]');
             rows1.unbind('click').bind('click', function (e) {
                 return false;
             });*/
        },
        // 双击行事件，删除行
        onDblClickCell: function (index, field, value) {
            /* $('#rightList').datagrid('deleteRow', index);
             return;*/
        }
    });

    $('#rightList1').datagrid("unselectAll");
}

function toRight1(row) {
    var contains = false;
    var datas = $('#rightList1').datagrid('getData');
    // 遍历行，不存在则添加
    for (var i = 0; i < datas.rows.length; i++) {
        if (datas.rows[i].id == row.id) {//数据中的id和渲染时的id相等
            contains = true;
            break;
        }
    }
    if (!contains) {
        $('#rightList1').datagrid('appendRow', {
            id: row.id,
            name: row.name
        });
    }
}

function toRightBatch1() {
    var ckInLeft = $('#leftList1').datagrid('getChecked');
    for (var i = 0; i < ckInLeft.length; i++) {
        toRight1(ckInLeft[i]);
    }
}

function delRightBatch1() {
    var checkedList = $('#rightList1').datagrid('getChecked');
    for (var j = checkedList.length - 1; j >= 0; j--) {
        var rowIndex = $('#rightList1').datagrid('getRowIndex', checkedList[j]);
        $('#rightList1').datagrid('deleteRow', rowIndex);
    }
    $('#rightList1').datagrid('clearChecked');
}

function submitSubjectForm() {


    $('#subjectForm').submit();

}
