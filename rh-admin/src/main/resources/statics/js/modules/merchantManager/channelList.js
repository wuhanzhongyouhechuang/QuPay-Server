$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'merchant/mgr/channelInfo/list',
        postData : {"merchantId" : 1000},
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', index: "id", width: 45, key: true},
            {label: '通道地址', name: 'url', sortable: false, width: 75},
            {label: '支付宝userId', name: 'aliUserId', sortable: false, width: 75},
            // {label: '权重', name: 'weight', sortable: false, width: 75},//权重字段预留deviceCode
            {label: '设备标识', name: 'deviceCode', sortable: false, width: 75},
            {label: '通道开关', name: 'flag', sortable: false, width: 60 , formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-success pointer" onclick="vm.updateChannel(\'' + value + '\',\'' + row.id + '\',\'' + row.merchantId + '\')">关闭通道</span>' :
                        '<span class="label label-danger pointer" onclick="vm.updateChannel(\'' + value + '\',\'' + row.id + '\',\'' + row.merchantId + '\')">开启通道</span>';
                }
            }
            // {
            //     label: '设置', name: 'id', width: 65,
            //     formatter: function (value, options, row) {
            //         return '<span class="label label-success pointer" onclick="vm.audit(\'' + $("#merchantNum").val() + '\')">详情</span>'
            //     }
            // }
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
            id: null,
            merchantName: null,
            merchantPhone: null,
            merchantDeptName: null,
            priFlag : 1,
            channelList : null
        },
        specList: [{
            url: null,
            weight: null,
            deviceCode : null,
            aliUserId : null
        }],
        showList: true,
        showList2: false,
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
        audit: function () {
            var id = $("#merchantNum").val().trim();
            if (id == null || id == "") {
                alert("请先选择商户");
                return;
            }
            vm.specList = null;
            vm.showList = false;
            vm.showList2 = true;
            vm.title = "详情";
            //获取角色信息
            this.getRoleList();
            vm.getChannel(id);
        },
        getRoleList: function () {
            $.get(baseURL + "sys/role/select", function (r) {
                vm.roleList = r.list;
            });
        },
        getChannel: function(value){
            $.get(baseURL + "merchant/mgr/channelInfo/" + value, function (r) {
                vm.specList = r.specList;
            });
        },
        saveOrUpdate: function () {
            var url = "merchant/mgr/updatePriChannel";
            vm.merchant.channelList =  vm.specList ;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.merchant),
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
        },
        getDept: function () {
            //加载部门树
            $.get(baseURL + "merchant/mgr/selectMerchant", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.merchantList);
            })
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
                    vm.merchant = node[0];
                    layer.close(index);
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            if ($("#merchantNum").val() == "" || $("#merchantNum").val() == null){
                alert("请先选择商户");
                return;
            }
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