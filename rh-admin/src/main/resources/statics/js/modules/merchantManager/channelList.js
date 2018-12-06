$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'merchant/mgr/channelInfo/list',
        postData : {"merchantId" : 1000},
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', index: "id", width: 45, key: true},
            {label: '通道地址', name: 'url', sortable: false, width: 75},
            // {label: '权重', name: 'weight', sortable: false, width: 75},//权重字段预留deviceCode
            {label: '设备标识', name: 'deviceCode', sortable: false, width: 75},
            {label: '通道开关', name: 'flag', sortable: false, width: 60 , formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-success pointer" onclick="vm.updateChannel(\'' + value + '\',\'' + row.id + '\',\'' + row.merchantId + '\')">关闭通道</span>' :
                        '<span class="label label-danger pointer" onclick="vm.updateChannel(\'' + value + '\',\'' + row.id + '\',\'' + row.merchantId + '\')">开启通道</span>';
                }
            }
        ],
        viewrecords: true,
        height: 720,
        rowNum: 50,
        rowList: [50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    vm.getDept();
    //配置状态字段背景色
});
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "merchantDeptId",
            rootPId: -1
        },
        key: {
            url: "nourl",
            name: "merchantName"
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
        merchant: {
            merchantId: null,
            merchantName: null,
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
        updateChannel: function(value, id , merchantId){
            confirm('确定关闭或开启通道开关？', function () {
                var dataMap = '{"id":"' + id + '" , "flag":"' + value + '"}';
                $.ajax({
                    type: "POST",
                    url: baseURL + "merchant/mgr/updateChannelFlag",
                    contentType: "application/json",
                    data: dataMap,
                    success: function (r) {
                        if (r.code === 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getDept: function () {
            //加载部门树
            $.get(baseURL + "merchant/mgr/selectMerchant", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.merchantList);
            })
        },
        getInfo: function (id) {
            $.get(baseURL + "abd/info/" + id, function (r) {
                vm.abd = r.abd;
            });
        },
        deptTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择合作商",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    $("#merchantName").val(node[0].merchantName);
                    $("#merchantNum").val(node[0].id);
                    layer.close(index);
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            $("#jqGrid").jqGrid('clearGridData');
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "merchantId": $("#merchantNum").val()
                },
                page: page
            }).trigger("reloadGrid");
            vm.getDept();
        }
    }
});