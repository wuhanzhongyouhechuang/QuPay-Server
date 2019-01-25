$(function () {
    $("#jqGrid").jqGrid({
        url: '../../merchant/mgr/queryMerchantListForDept',
        sortname: 'id',
        sortable: true,
        sortorder: 'desc',
        datatype: "json",
        type: 'post',
        colModel: [
            {label: '商户ID', name: 'id', index: "id", width: 45, key: true},
            {label: '商户名称', name: 'merchantName', sortable: false, width: 75},
            {label: '上级商户', name: 'merchantDeptName', sortable: false, width: 75},
            {label: '联系方式', name: 'merchantPhone', sortable: false, width: 75},
            {label: '商户密钥', name: 'merchantKey', width: 90},
            // { label: '手机号', name: 'mobile', width: 100 },
            {
                label: '状态', name: 'status', width: 60, formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-success">通过验证</span>' :
                        '<span class="label label-danger">其他</span>';
                }
            }
        ],
        viewrecords: true,
        height: 720,
        rowNum: 10,
        rowList: [10 , 20 , 30],
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
            // var newHeight = $(window).height() - $(".gridPanel").offset().top - 66;
            // $(".ui-jqgrid .ui-jqgrid-bdiv").css("cssText","height: "+newHeight+"px!important;");
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
        reload: function (event) {
            vm.showList = true;
            $("#jqGrid").jqGrid('clearGridData');
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "tradeid": $("#tradeid").val(),
                    "orderid": $("#orderid").val(),
                    "starttime": $("#starttime").val(),
                    "endtime": $("#endtime").val(),
                    "merchantid": $("#merchantid").val(),
                    "status": $("#status").val(),
                    "merchantdept": $("#merchantNum").val(),
                    "tradeno": $("#tradeno").val()
                },
                page: page
            }).trigger("reloadGrid");
        },
        reload2: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'tradeid': $("#tradeid").val(),
                    'orderid': $("#orderid").val(),
                    'starttime': $("#starttime").val(),
                    'endtime': $("#endtime").val(),
                    'merchantid': $("#merchantid").val(),
                    'status': $("#status").val(),
                    'merchantdept': $("#merchantNum").val(),
                    'tradeno': $("#tradeno").val()
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});

