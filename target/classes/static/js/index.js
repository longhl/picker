var projectId = $('#projectId').val();
var version;

$(function () {

    /*    $('#draw-list').datagrid({
            url:'/inst/drawList',
            columns:[[
                {field:'id',title:'Code',width:100},
                {field:'name',title:'Name',width:100},
                {field:'flag',title:'Price',width:100}
            ]]
        });*/

    $('#draw-list').datagrid({
        idField: "id",
        fitColumns: true,
        rownumbers: true,
        collapsible: false,
        pagination: false,//分页栏
        showHeader: true,//显示头部
        singleSelect: true,//单选
        //url: '/inst/drawList',
        method: 'get',
        datatype: 'json',
        queryParams: {
            projectId: projectId,
            version: version
        },
        columns: [[
            {field: 'id', title: '主键', width: "20%", hidden: true},
            {field: 'name', title: '供方列表', width: "80%"},
            {
                field: 'flag', title: '是否中签', width: "20%", formatter: function (value, row, index) {
                var delHtml = "<span><font color='red'>是</font></span>";
                if (value == '1') {
                    return delHtml;
                } else if (value == '0') {
                    return "否";
                } else {
                    return "";
                }
            }
            }
        ]],
        onLoadSuccess: function (data) {
        }
    });

    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];

        if (e && e.keyCode == 119) {
            console.log('F8');
            $('#drawFlag').attr("value", '0');
        }
        if (e && e.keyCode == 120) {
            console.log('F9');
            $('#drawFlag').attr("value", '1');
        }
    }
});


// 执行抽签
function actDraw() {
    var p_id = $('#projectId').val();
    var drawFlag = $('#drawFlag').val();
    $.ajax({
        url: '/draw/act',
        type: 'POST',
        dataType: 'json',
        data: {pId: p_id, drawFlag: drawFlag},
        success: function (data) {
            var ids = data.data;

            var strs = new Array(); //定义一数组
            strs = ids.split(","); //字符分割

            var datas = $('#draw-list').datagrid('getData');

            for (var i = 0; i < datas.rows.length; i++) {
                updateRow(i, '0');
                for (var j = 0; j < strs.length; j++) {
                    if (datas.rows[i].id == strs[j]) {
                        updateRow(i, '1');
                    }
                }
            }
        }
    })
}

function updateRow(index, val) {
    $('#draw-list').datagrid('updateRow', {
        index: index,
        row: {
            flag: val
        }
    });
}

function openSubjectView() {
    openDialog("/subject/view");
}

function openDialog(url) {
    var width = window.screen.width * 0.5;
    var height = window.screen.height * 0.48;
    window.open(url, "", 'height=' + height + ", width=" + width + ", top=" + height * 0.2 + ", left=" + window.screen.width * 0.2
        + ",toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no"
    );
}

