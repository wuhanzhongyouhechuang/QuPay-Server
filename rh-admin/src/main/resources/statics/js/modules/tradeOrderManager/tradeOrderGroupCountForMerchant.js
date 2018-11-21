$(function () {
    vm.getDept();
    //配置状态字段背景色
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
        getDept: function () {
            console.log("============>>加载部门树");
            //加载部门树
            $.get(baseURL + "merchant/dept/listForMerchant", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.dept.deptId);
                if (node != null) {
                    ztree.selectNode(node);
                    vm.dept.parentName = node.name;
                    // vm.merchant.merchantdeptid = node.id;
                }
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
                    $("#merchantName").val(node[0].name);
                    $("#merchantNum").val(node[0].deptId);
                    layer.close(index);
                }
            });
        },
        reload: function (event) {
            vm.getDept();
        },
        reload2: function (event) {

        }
    }
});