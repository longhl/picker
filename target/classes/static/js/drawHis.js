function getDrawHis() {
    //开启模态框
    $('#modal-drawHis').window('open');
    $('#drawHisTab').datagrid({
        view: detailview,
        url: '/draw/his',
        singleSelect: "true",
        fitColumns: "true",
        striped: true,
        checkOnSelect: 'true',
        method: 'get',
        columns: [[
            {field: 'id', title: '主键', width: 200, hidden: true},
            {field: 'projectId', title: '项目id', width: 100, align: 'right', hidden: true},
            {field: 'projectName', title: '项目名称', width: '57%'},
            /*{field: 'subjectId', title: '科目ID', width: '20%', hidden: true},
            {field: 'subjectName', title: '科目', width: '24%', hidden: true},*/
            {field: 'drawer', title: '抽签人', width: '10%'},
            {field: 'recorder', title: '记录人', width: '10%'},
            {field: 'date', title: '摇号日期', width: '12%'},
            {field: 'op', title: '操作', width: '8%', align: 'center', formatter: delDrawHis}
        ]],
        detailFormatter: function (index, row) {
            return '<div style="padding:2px"><table id="ddv-' + index + '" style="width: 100%;"></table></div>';
        },
        onExpandRow: function (index, row) {
            console.log("row = " + JSON.stringify(row))
            $('#ddv-' + index).datagrid({
                url: '/draw/hisDetail?id=' + row.id,
                fitColumns: true,
                singleSelect: true,
                rownumbers: true,
                loadMsg: '',
                height: 'auto',
                method: 'get',
                columns: [[
                    {field: 'id', title: '供方主键', width: 100, hidden: true},
                    {field: 'name', title: '中签单位', width: '98%', align: 'left'}/*,
                    {
                        field: 'flag', title: '是否中签', width: '18%', formatter: function (value, row, index) {
                        var delHtml = "<span><font color='red'>是</font></span>";
                        if (value == '1') {
                            return delHtml;
                        } else if (value == '0') {
                            return "否";
                        } else {
                            return "";
                        }
                    }
                    }*/
                ]],
                onResize: function () {
                    $('#drawHisTab').datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess: function () {
                    $('#drawHisTab').datagrid("selectRow", index)
                    setTimeout(function () {
                        $('#drawHisTab').datagrid('fixDetailRowHeight', index);
                    }, 0);
                }
            });
            $('#drawHisTab').datagrid('fixDetailRowHeight', index);
        }
    });

    initCombobox1();
};

function formatOper(val, row, index) {
    return '<a href="#" onclick="editUser(' + index + ')">修改</a>';
}

function editUser(index) {
    alert("ddd");
}

function initCombobox1() {
    $('#q-subject').combobox({
        url: '/subject/listAll',
        method: 'get',
        valueField: 'id',
        textField: 'name',
        onSelect: function (record) {

        }
    });
}

function queryDrawHisList() {
    var projectName = $('#q-project').val();
    var subject = $('#q-subject').val();
    var dateStart = $('#q-dateStart').val();
    var dateEnd = $('#q-dateEnd').val();
    $('#drawHisTab').datagrid('load', {
        'sp[projectName]': projectName,
        'sp[subject]': subject,
        'sp[dateStart]': dateStart,
        'sp[dateEnd]': dateEnd
    });
}

function delDrawHis(val, row, index) {
    return '<a href="#" onclick="removeDrawHis(' + row.id + ')">删除</a>';
}

function removeDrawHis(hisId) {
    $.messager.confirm('Confirm', '删除该历史纪录？', function (r) {
        if (r) {
            $.post('/draw/del', {hisId: hisId}, function (result) {
                if (result.code == 'success') {
                    $('#drawHisTab').datagrid('reload');	// reload the user data
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


function downloadDrawHisList1() {

    var projectName = $('#q-project').val();
    var subject = $('#q-subject').val();
    var dateStart = $('#q-dateStart').val();
    var dateEnd = $('#q-dateEnd').val();

    var form = $("<form>");
    form.attr("style", "display:none");
    form.attr("method", "post");
    form.attr("action", "/draw/download");
    createHidden(form, 'sp[projectName]', projectName);
    createHidden(form, 'sp[subject]', subject);
    createHidden(form, 'sp[dateStart]', dateStart);
    createHidden(form, 'sp[dateEnd]', dateEnd);

    $("body").append(form);
    form.submit();
    form.remove();
}

function createHidden(form, name, value) {
    var input = $("<input>");
    input.attr("type", "hidden");
    input.attr("name", name);
    input.attr("value", value);
    form.append(input);
    return form;
}


function downloadDrawHisList() {

    var projectName = $('#q-project').val();
    var subject = $('#q-subject').val();
    var dateStart = $('#q-dateStart').val();
    var dateEnd = $('#q-dateEnd').val();

    postDownLoadFile({
        url: "/draw/download", //这里写后台请求的url
        data: {
            'sp[projectName]': projectName,
            'sp[subject]': subject,
            'sp[dateStart]': dateStart,
            'sp[dateEnd]': dateEnd
        },
        method: 'post'
    });
}

//后台返回成功后回填的数据
var postDownLoadFile = function (options) {
    var config = $.extend(true, {
        method: 'post'
    }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form
            .append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
}


