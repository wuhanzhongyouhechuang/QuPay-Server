$(function () {

});

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: 0
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
        showList: true,
        title: "XX省企业技术中心评定表",
        gb: {
            id: null,
            pid: null,
            code: null,
            name: null
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },

        getgbTree: function () {
            //加载Gb编码树
            $.get(baseURL + "apply/mygb/list", function (r) {
                ztree = $.fn.zTree.init($("#gbTree"), setting, r);
                // alert(ztree)
                var node = ztree.getNodeByParam("id", vm.gb.id);
                if (node != null) {
                    ztree.selectNode(node);
                    vm.gb.name = node.name;
                }
            })
        },

        gbTree: function () {
            vm.getgbTree();
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择行业代码",
                area: ['320px', '460px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#gbLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    // vm.dept.parentId = node[0].deptId;
                    vm.gb.id = node[0].id;
                    vm.gb.name = node[0].name;
                    layer.close(index);
                }
            });
        },

        reload: function () {
            vm.showList = true;
        }
    }
});