$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [
            {label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true},
            {label: '用户名', name: 'username', sortable: true, width: 75},
            {label: '所属部门', name: 'deptName', sortable: false, width: 75},
            {label: '邮箱', name: 'email', width: 90},
            {label: '手机号', name: 'mobile', width: 100},
            {
                label: '状态', name: 'status', width: 60, formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">正常</span>';
                }
            },
            {label: '创建时间', name: 'createTime', index: "create_time", width: 85},
            {
                label: '详情', name: 'userId', width: 65,
                formatter: function (value, options, row) {
                    return '<span class="label label-danger pointer" onclick="vm.audit(\'' + value + '\')">详情</span>'
                }
            }
        ],
        viewrecords: true,
        height: 720,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
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
            // var ids=$("#jqGrid").jqGrid('getDataIDs');
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
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
var ztree2;
//数据树
var data_ztree;
var data_setting = {
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
    },
    check: {
        enable: true,
        nocheckInherit: true,
        chkboxType: {"Y": "", "N": ""}
    }
};
var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            username: null
        },
        showList: true,
        showList2: false,
        showList3: false,
        title: null,
        roleList: {},
        user: {
            status: 1,
            deptId: null,
            deptName: null,
            roleIdList: [],
            merchantDeptId : null,
            merchantDeptName: null
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },

        audit: function (value) {
            var userId = value;
            if (userId == null) {
                return;
            }

            vm.showList = false;
            vm.showList3 = true;
            vm.title = "详情";

            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },

        add: function () {
            vm.showList = false;
            vm.showList3 = true;
            vm.title = "新增";
            vm.roleList = {};
            vm.user = {deptName: null, deptId: null, status: 1, roleIdList: [],merchantDeptId : null,merchantDeptName: null};

            //获取角色信息
            this.getRoleList();

            vm.getDept();
            vm.getMerchantDept();

            vm.getDataTree();
        },
        getDataTree: function (deptId) {
            //加载菜单树
            $.get(baseURL + "merchant/dept/selectParent", function (r) {
                data_ztree = $.fn.zTree.init($("#dataTree"), data_setting, r.deptList);
                if (null == deptId){
                    //展开所有节点
                    data_ztree.expandAll(false);
                }else {
                    //勾选角色所拥有的部门数据权限str = str.substring(0, str.lastIndexOf(','));
                    var deptIds = (deptId.substring(0, deptId.lastIndexOf(","))).split(",");
                    for (var i = 0; i < deptIds.length; i++) {
                        var node = data_ztree.getNodeByParam("deptId", deptIds[i]);
                        data_ztree.checkNode(node, true, false);
                    }
                }

            });
        },
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/list", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if (vm.user.deptId === 8){
                    $("#deptDataTree").attr("style","display:block;");
                }
                if (node != null) {
                    ztree.selectNode(node);
                    vm.user.deptName = node.name;
                }
            })
        },
        getMerchantDept: function () {
            //加载部门树
            $.get(baseURL + "merchant/dept/select", function (r) {
                ztree2 = $.fn.zTree.init($("#deptTree2"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.user.merchantDeptId);
                if (node != null) {
                    ztree2.selectNode(node);
                    vm.user.merchantDeptName = node.name;
                }
            })
        },
        update: function () {
            var userId = getSelectedRow();
            if (userId == null) {
                return;
            }

            vm.showList = false;
            vm.showList3 = true;
            vm.title = "修改";
            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        del: function () {
            var userIds = getSelectedRows();
            if (userIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function (r) {
                        if (r.code == 0) {
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
        saveOrUpdate: function () {
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            //获取选择的数据
            var nodes = data_ztree.getCheckedNodes(true);
            var deptIdList = "";
            for (var i = 0; i < nodes.length; i++) {
                deptIdList += nodes[i].deptId+",";
            }
            vm.user.merchantDeptId = deptIdList;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
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
        getUser: function (userId) {
            $.get(baseURL + "sys/user/info/" + userId, function (r) {
                vm.user = r.user;
                vm.user.password = null;
                vm.getDept();
                vm.getMerchantDept();
                vm.getDataTree(vm.user.merchantDeptId);
            });
        },
        getRoleList: function () {
            $.get(baseURL + "sys/role/select", function (r) {
                vm.roleList = r.list;
            });
        },
        deptTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;
                    if (node[0].deptId === 8){
                        // console.log("====>"+node[0].deptId);
                        // $("#merchantDept").attr("style","display:block;");
                        $("#deptDataTree").attr("style","display:block;");
                    }else {
                        // console.log("====>"+node[0].deptId);
                        // $("#merchantDept").attr("style","display:none;");
                        $("#deptDataTree").attr("style","display:none;");
                        vm.user.merchantDeptName = null;
                        vm.user.merchantDeptId = null;
                     }
                    layer.close(index);
                }
            });
        },
        merchantDeptTree:function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择商户",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer2"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree2.getSelectedNodes();
                    //选择上级部门
                    // $("#merchantDeptId").val(node[0].deptId);
                    //                     // $("#merchantDeptName").val(node[0].name);
                    vm.user.merchantDeptName = node[0].name;
                    vm.user.merchantDeptId = node[0].deptId;
                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            vm.showList2 = false;
            vm.showList3 = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'username': vm.q.username},
                page: page
            }).trigger("reloadGrid");
            $("#deptDataTree").attr("style","display:none;");
        }
    }
});