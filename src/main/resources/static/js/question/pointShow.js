//拓扑
var network;
// 创建节点对象
var nodes;
// 创建连线对象
var edges;
// 已扩展的节点
var nodeExtendArr = new Array();


$(function () {
    $(".main-contain").css("height", $(window).height() * 0.9)
    $("#network_id").css("height", "100%")

    $("#pointDetail").hide()


    $(document).on("click", "#pointDetail .jumpBtn", function () {
        window.location.href = "/record/doQuestion?nodeId=" + $("#pointDetail .nodeId").text();
    })


    layui.use('layer', function () {
        layer = layui.layer;
        init();
        //修改初始缩放
        network.moveTo({scale: 0.8});
        //先初始化一个节点
        $.ajax({
            url: '/neo4jShow/getTopPoint',
            type: "post",
            async: false,
            success: function (ret) {
                if (ret) {
                    //createNetwork({nodes: ret.nodeList});
                    var newNodes = [];
                    for (var i = 0; i < ret.nodeList.length; i++) {
                        var m = {
                            "nodeId": ret.nodeList[i].nodeId,
                            // "知识点Id": ret.nodeList[i].pointId,
                            "知识点": ret.nodeList[0].pointDetail
                        }
                        newNodes.push(m)
                    }
                    createNetwork({nodes: newNodes});
                } else {
                    layer.msg("查询失败");
                }
            }
        });
        //拖拉节点
        network.on("dragEnd", function (params) {
            if (params.nodes && params.nodes.length > 0) {
                network.clustering.updateClusteredNode(params.nodes[0], {physics: false});
            }
        });
        //点击节点显示详情
        network.on("click", function (params) {
            var nodeId = params.nodes[0];
            if (nodeId != null) {
                $.ajax({
                    url: '/neo/findPointById',
                    type: "post",
                    data: {
                        "id": nodeId
                    },
                    success: function (ret) {
                        if (ret) {
                            $("#pointDetail .textShow .nodeId").text(nodeId);
                            $("#pointDetail").show()
                            if (ret.data.pointDetail != "") {
                                $("#pointDetail .textShow .pointDetail").show()
                                $("#pointDetail .textShow .pointDetail").text("内容：" + ret.data.pointDetail);
                            }
                            else $("#pointDetail .textShow .pointDetail").hide()

                            if (ret.data.chapter != "") {
                                $("#pointDetail .textShow .chapter").show()
                                $("#pointDetail .textShow .chapter").text("章节：" + ret.data.chapter);
                            }
                            else $("#pointDetail .textShow .chapter").hide()

                            if (ret.data.isbn != "") {
                                $("#pointDetail .textShow .isbn").show()
                                $("#pointDetail .textShow .isbn").text("ISBN：" + ret.data.isbn);
                            }
                            else $("#pointDetail .textShow .isbn").hide()

                            if (ret.data.grade != "") {
                                $("#pointDetail .textShow .grade").show()
                                $("#pointDetail .textShow .isbgraden").text("年级：" + ret.data.grade);
                            }
                            else $("#pointDetail .textShow .grade").hide()

                            if (ret.data.distribution != "") {
                                $("#pointDetail .textShow .distribution").show()
                                $("#pointDetail .textShow .distribution").text("考试要求分布：" + ret.data.distribution);
                            }
                            else $("#pointDetail .textShow .distribution").hide()

                            if (ret.data.frequency != "") {
                                $("#pointDetail .textShow .frequency").show()
                                $("#pointDetail .textShow .frequency").text("频次：" + ret.data.frequency);
                            }
                            else $("#pointDetail .textShow .frequency").hide()
                        } else {
                            layer.msg("查询失败");
                        }
                    }
                });
            }

        });
        //双击扩展
        network.on("doubleClick", function (params) {
            // 取出当前节点在Vis的节点ID、
            var nodeId = params.nodes[0];
            if (nodeId != null && nodeId != "") {
                if (nodeExtendArr.indexOf(nodeId) != -1) {
                    layer.msg("该节点已经扩展");
                } else {
                    getData(nodeId);
                }
            }

        });
    })
});

function init() {
    // 创建节点对象
    nodes = new vis.DataSet([]);
    // 创建连线对象
    edges = new vis.DataSet([]);
    // 创建一个网络拓扑图  不要使用jquery获取元素
    var container = document.getElementById('network_id');
    var data = {nodes: nodes, edges: edges};
    //全局设置，每个节点和关系的属性会覆盖全局设置
    var options = {
        //设置节点形状
        nodes: {
            shape: 'dot',//采用远点的形式
            size: 30,
            font: {
                face: 'Microsoft YaHei'
            }
        },
        // 设置关系连线
        edges: {
            font: {
                face: 'Microsoft YaHei'
            }
        },
        //设置节点的相互作用
        interaction: {
            //鼠标经过改变样式
            hover: true
            //设置禁止缩放
            //zoomView:false
        },
        //力导向图效果
        physics: {
            enabled: true,
            barnesHut: {
                gravitationalConstant: -4000,
                centralGravity: 0.3,
                springLength: 120,
                springConstant: 0.04,
                damping: 0.09,
                avoidOverlap: 0
            }
        }
    };
    network = new vis.Network(container, data, options);
}

//获取id扩展后的数据
function getData(id) {
    layui.use('layer', function () {
        layer = layui.layer;
        var tipMsg = layer.msg('数据加载中，请稍等...', {icon: 16, shade: [0.1, '#000'], time: 0, offset: '250px'});
        //该节点已扩展
        nodeExtendArr.push(id);
        $.ajax({
            url: '/neo4jShow/getPointPath',
            type: "post",
            data: {
                id: id //当前节点id
            },
            success: function (ret) {
                layer.close(tipMsg);
                if (ret) {
                    // createNetwork({nodes: ret.nodeList, edges: ret.edgeList});
                    var newNodes = [];
                    for (var i = 0; i < ret.nodeList.length; i++) {
                        var m = {
                            "nodeId": ret.nodeList[i].nodeId,
                            // "知识点Id": ret.nodeList[i].pointId,
                            "知识点": ret.nodeList[0].pointDetail
                        }
                        newNodes.push(m)
                    }

                    createNetwork({nodes: newNodes, edges: ret.edgeList});
                } else {
                    layer.msg("查询失败");
                }
            }
        });
    })
}

//扩展节点 param nodes和relation集合
function createNetwork(param) {
    layui.use('layer', function () {
        layer = layui.layer;
        //可以试试注释掉去重的方法看看效果
        if (param.nodes && param.nodes.length > 0) {
            //去除已存在的节点  以“id”属性为例删除重复节点，根据具体的属性自行修改
            for (var i in network.body.data.nodes._data) {
                var nodeTemp = network.body.data.nodes._data[i];
                param.nodes = deleteValueFromArr(param.nodes, "nodeId", nodeTemp.id);
            }
            if (param.nodes && param.nodes.length > 0) {
                //添加节点
                for (var i = 0; i < param.nodes.length; i++) {
                    var node = param.nodes[i];
                    //控制背景色 不同类型的节点颜色不同
                    var background = "#97C2FC";
                    if (node.name && node.name != "") {
                        background = "#FFD86E";
                    }
                    else if (node.title && node.title != "") {
                        background = "#6DCE9E";
                    }
                    //拼接返回的结果显示在图上
                    var label = "";
                    for (var n in node) {
                        label += n + ":" + node[n] + "\n";
                    }
                    nodes.add({
                        id: node.nodeId,
                        label: label,
                        color: {
                            background: background
                        }
                    });
                }
            } else {
                layer.msg("无扩展信息");
            }
        }
        if (param.edges && param.edges.length > 0) {
            //去除已存在的关系
            for (var i in network.body.data.edges._data) {
                var edgeTemp = network.body.data.edges._data[i];
                param.edges = deleteValueFromArr(param.edges, "edgeId", edgeTemp.id);
            }
            if (param.edges && param.edges.length > 0) {
                //添加关系
                for (var i = 0; i < param.edges.length; i++) {
                    var edge = param.edges[i];
                    //拼接返回的结果显示在图上  根据数据库属性存储的格式进行调整
                    var label = "";
                    for (var n in edge.roles) {
                        label += edge.roles[n] + " ";
                    }
                    edges.add({
                        id: edge.edgeId,
                        arrows: 'to',
                        from: edge.edgeFrom,
                        to: edge.edgeTo,
                        label: label,
                        font: {align: "middle"},
                        length: 150
                    });
                }
            }
        }
    })
}

//根据对象组数中的某个属性值进行过滤删除
//arrName数组名  field过滤的字段   keyValue字段值
function deleteValueFromArr(arrName, field, keyValue) {
    if (arrName == null || arrName.length == 0) {
        return null;
    }
    for (var i = 0; i < arrName.length; i++) {
        if (arrName[i][field] == keyValue) {
            arrName.splice(i, 1);
        }
    }
    return arrName;
}

//根据对象数组中的某个属性值获取过滤后的数组
//arrName数组名  field过滤的字段   keyValue字段值
function getArrFromArr(arrName, field, keyValue) {
    var arrReturn = [];
    if (arrName == null || arrName.length == 0) {
        return arrReturn;
    }
    var obj;
    for (var item = 0; item < arrName.length; item++) {
        obj = arrName[item];
        if (obj[field] == keyValue) {
            arrReturn.push(obj);
        }
    }
    return arrReturn;
}
