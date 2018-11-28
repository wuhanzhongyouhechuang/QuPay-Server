$(function () {
    $("#jqGrid").jqGrid({
        url: '../../merchant/mgr/hasOrder',
        sortname: 'merchantDeptId',
        sortable: true,
        sortorder: 'desc',
        datatype: "json",
        type: 'post',
        colModel: [
            {label: '部门ID', name: 'merchantDeptId', index: 'merchantDeptId', width: 50},
            {label: '部门名称', name: 'merchantDeptName', index: 'merchantDeptName', width: 50},
            {label: '导出昨日订单excel' ,name: 'merchantDeptId', width: 50 ,
                formatter: function (value, options, row) {
                    return '<span class="label label-success pointer" onclick="vm.export(\'' + value + '\')">导出</span>'
                }}
        ],
        viewrecords: true,
        height: 720,
        rowNum: 30,
        rowList: [30, 50, 80],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount",
            userdata: "countDate"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
    });
});
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            tradeid: null,
            orderid: null,
            merchantid: null,
            starttime: null,
            endtime: null,
            tradestatus: null
        },
        dept: {
            parentName: null,
            deptId: null,
            parentId: 0,
            orderNum: 0
        },
        showList: true,
        title: null,
        abd: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        getInfo: function (id) {
            $.get(baseURL + "abd/info/" + id, function (r) {
                vm.abd = r.abd;
            });
        },
        export: function(deptId){
            window.location.href = baseURL + "/merchant/mgr/export" + "?merchantDeptId="+deptId;
        },
        reload: function (event) {
            vm.showList = true;
            $("#jqGrid").jqGrid('clearGridData');
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                },
                page: page
            }).trigger("reloadGrid");
            vm.getDept();
        },
        reload2: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});