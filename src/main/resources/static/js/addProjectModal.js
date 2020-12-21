$(function () {

});

function getProjectList() {
    //开启模态框
    $('#modal-projectManage').window('open')

    $('#projectList').datagrid({
        idField: "id",
        fitColumns: true,
        rownumbers: true,
        pagination: false,//分页栏
        showHeader: true,//显示头部
        singleSelect: true,//单选
        url: '/project/listAll',
        method: 'get',
        datatype: 'json',
        columns: [[
            {field: 'id', title: '主键', width: 30, checkbox: true},
            {field: 'name', title: '项目名称', width: 30},
            /*{
                field: 'subName', title: '科目', width: 20, formatter: function (val, row, index) {
                //console.log(JSON.stringify(row.subject));
                var subject = row.subject;
                if (subject) {
                    return '<span>' + subject.name + '</span>'
                } else {
                    return '';
                }
                }, hidden: true
            },*/
            /*{field: 'drawNum', title: '中签数', width: 15, hidden: true},*/
            //{field: 'amount', title: '抽签数', width: 20},
            {field: 'drawer', title: '抽签人', width: 15},
            {field: 'recorder', title: '记录人', width: 15},
            {field: 'date', title: '日期', width: 15}
        ]],
        onLoadSuccess: function (data) {
        },
        // 双击行事件
        onDblClickCell: function (index, field, value) {
            var datas = $('#leftList').datagrid('getData');
            toRight(datas.rows[index]);
            return;
        }
    });
};

function initLeft() {
    $('#leftList').datagrid({
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
            var datas = $('#leftList').datagrid('getData');
            toRight(datas.rows[index]);
            return;
        }
    });
    $('#leftList').datagrid("unselectAll")
}

function initRight(subId,levelId) {

    var url;
    if (subId != null && levelId != null) {
        url = '/subject/getCurrentInstList?id=' + subId + '&levelId=' + levelId;
    } else if (subId){
        url = '/subject/getCurrentInstList?id=' + subId;
    } else {
        url = "/subject/getCurrentInstList";
    }

    $('#rightList').datagrid({
        idField: "id",
        fitColumns: true,
        rownumbers: true,
        collapsible: false,
        pagination: false,//分页栏
        showHeader: true,//隐藏头部
        checkbox: true,
        url: url,
        method: 'get',
        datatype: 'json',
        columns: [[
            {field: 'id', title: 'id', width: 30, checkbox: true},
            {field: 'name', title: '参与竞标方', width: 30}
        ]],
        onLoadSuccess: function (data) {
            var s = $("#instMngList").datagrid('getPanel');
            var rows1 = s.find('tr.datagrid-row td[field!=state1]');
            rows1.unbind('click').bind('click', function (e) {
                return false;
            });
        },
        // 双击行事件，删除行
        onDblClickCell: function (index, field, value) {
            $('#rightList').datagrid('deleteRow', index);
            return;
        }
    });

    $('#rightList').datagrid("unselectAll")
}

function toRight(row) {
    var contains = false;
    var datas = $('#rightList').datagrid('getData');
    // 遍历行，不存在则添加
    for (var i = 0; i < datas.rows.length; i++) {
        if (datas.rows[i].id == row.id) {//数据中的id和渲染时的id相等
            contains = true;
            break;
        }
    }
    if (!contains) {
        $('#rightList').datagrid('appendRow', {
            id: row.id,
            name: row.name
        });
    }
}

function toRightBatch() {
    var ckInLeft = $('#leftList').datagrid('getChecked');
    for (var i = 0; i < ckInLeft.length; i++) {
        toRight(ckInLeft[i]);
    }
}

function delRightBatch() {
    var checkedList = $('#rightList').datagrid('getChecked');
    for (var j = checkedList.length - 1; j >= 0; j--) {
        var rowIndex = $('#rightList').datagrid('getRowIndex', checkedList[j]);
        $('#rightList').datagrid('deleteRow', rowIndex);
    }
    $('#rightList').datagrid('clearChecked');
}

function dateFormatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '/' + (m < 10 ? ('0' + m) : m) + '/' + (d < 10 ? ('0' + d) : d);
};

function myparser(s) {
    if (!s) {
        return new Date();
    }
    var ss = (s.split('/'));
    var y = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var d = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    } else {
        return new Date();
    }
};

function addProject1() {

    // 清空搜索框
    $('#subject').textbox('setValue', '');
    $('#projectName').textbox('setValue', '');
    $('#drawNum').textbox('setValue', '');
    $('#date').textbox('setValue', '');
    $('#drawer').textbox('setValue', '');
    $('#recorder').textbox('setValue', '');
    $('#ss-inst').textbox('setValue', '');

    $('#modal-addProject').window('open');

    initCombobox();
    initSubInstLevelCombobox();

    initLeft();
    initRight();

    $('#addProjectForm').form({
        url: '/project/add',
        onSubmit: function (param) {
            var datas = $('#rightList').datagrid('getData');

            if (datas.rows.length == 0) {
                $.messager.alert('提示', '参与抽签的供方列表为空！', 'warning');
                return false;
            }

            var rows = JSON.stringify(datas.rows);
            rows = rows.replace(/\"/g, "'");    //把json字符串中的双引号替换为 单引号
            param.list = JSON.stringify(rows);

            return $(this).form('enableValidation').form('validate');
        },
        success: function (result) {
            var result = eval('(' + result + ')');

            // 将新项目插入项目列表
            $('#projectList').datagrid('insertRow', {
                index: 0,
                row: result.data
            });

            // 关闭模态框

            $('#modal-addProject').window('close');
        }
    });

}

function delProject() {
    var row = $('#projectList').datagrid('getSelected');
    if (row) {
        $.messager.confirm('Confirm', '确认删除项目？', function (r) {
            if (r) {
                $.post('/project/del', {id: row.id}, function (result) {

                    if (result.code == 'success') {
                        console.log('success')
                        $('#projectList').datagrid('reload');	// reload the user data
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
};

function submit() {
    // 提交表单
    $('#addProjectForm').submit();
    // 刷新列表

};

//加载项目到主页
function loadProject() {
    var row = $('#projectList').datagrid('getSelected');
    if (row) {
        $.post('/project/get', {projectId: row.id}, function (result) {
            $('#modal-projectManage').window('close');
            $('#sp_projectName').html(result.name);
            var subject = result.subject;
            $('#sp_subjectName').html(subject.name);
            $('#sp_drawer').html(result.drawer);
            $('#sp_drawNum').html(result.drawNum);
            $('#sp_recorder').html(result.recorder);
            $('#projectId').attr("value", result.id);
            $('#draw-list').datagrid('loadData', result.elements);
        }, 'json');
    }
    ;
};

function doSearch1(value) {

    $('#leftList').datagrid('load', {
        'name': value,
        'sp[name]': value
    });
}

function initCombobox() {
    $('#subject').combobox({
        url: '/subject/listAll',
        method: 'get',
        valueField: 'id',
        textField: 'name',
        onSelect: function (record) {
            $('#subInstLevel').combobox('clear');
            $('#subInstLevel').combobox('reload', '/subject/level?subId=' + record.id);
        }
    });
};

function initSubInstLevelCombobox() {
    $('#subInstLevel').combobox({
        url: '/subject/level',
        method: 'get',
        valueField: 'levelId',
        textField: 'levelName',

        multiple:true,

        onSelect: function (row) {
            var opts = $(this).combobox('options');
            //获取选中的值的values
            $("#subInstLevel").val($(this).combobox('getValues'));

            //设置选中值所对应的复选框为选中状态
            var el = opts.finder.getEl(this, row[opts.valueField]);
            el.find('input.combobox-checkbox')._propAttr('checked', true);

            //initRight(record.subId, record.levelId);

            addIntoRight(row.subId, row.levelId);
        },
        onUnselect: function (row) {//不选中一个选项时调用
            var opts = $(this).combobox('options');
            //获取选中的值的values
            $("#subInstLevel").val($(this).combobox('getValues'));

            var el = opts.finder.getEl(this, row[opts.valueField]);
            el.find('input.combobox-checkbox')._propAttr('checked', false);

            delFromRight(row.subId, row.levelId);
        },
        formatter: function (row) { //formatter方法就是实现了在每个下拉选项前面增加checkbox框的方法
            var opts = $(this).combobox('options');
            return '<input type="checkbox" class="combobox-checkbox">' + row[opts.textField]
        },
        onLoadSuccess : function(){
            var data = $('#subInstLevel').combobox('getData');
            if(data.length > 0){
                $('#subInstLevel').combobox('select',data[0].levelId);
            }
        }
    });

    //$.fn.combobox.defaults.multiple = true
};

function addIntoRight(subId,levelId) {
    $.ajax({
        url: '/subject/getCurrentInstList',
        type: 'POST',
        dataType: 'json',
        data: {id: subId, levelId: levelId},
        success: function (data) {
            if(data){
                for (var i = 0; i < data.length; i++){
                    toRight(data[i]);
                }
            }
        }
    })
};

function delFromRight(subId,levelId){
    $.ajax({
        url: '/subject/getCurrentInstList',
        type: 'POST',
        dataType: 'json',
        data: {id: subId, levelId: levelId},
        success: function (data) {
            if(data){
                //var checkedList = $('#rightList').datagrid('getChecked');
                for (var j = data.length - 1; j >= 0; j--) {
                    var rowIndex = $('#rightList').datagrid('getRowIndex', data[j].id);
                    $('#rightList').datagrid('deleteRow', rowIndex);
                }
                //$('#rightList').datagrid('clearChecked');
            }
        }
    });

};