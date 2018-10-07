
$(function () {
    $.ajax({
        type: "Post",
        url: "http://mazhaoyang.cn/index.php/home/output/GangkouGetTree",
        dataType: "json",
        success: function (result) {
            $('#pt-tree').treeview({
                data: [
                    {
                        text: 'Parent 1',
                        tags: ['2'],
                        nodes: [
                            {
                                text: 'Child 1',
                                tags: ['3']
                            },
                            {
                                text: 'Child 2',
                                tags: ['3']
                            }
                        ]
                    },
                    {
                        text: 'Parent 2',
                        tags: ['7'],
                        nodes: [
                            {
                                text: 'Child 1',
                                tags: ['3']
                            },
                            {
                                text: 'Child 2',
                                tags: ['3']
                            }
                        ]
                    },
                    {
                        text: 'Parent 3',
                        href: '#demo',
                        tags: ['11'],
                        nodes: [
                            {
                                text: 'Child 1',
                                tags: ['3']
                            },
                            {
                                text: 'Child 2',
                                tags: ['3']
                            }
                        ]
                    }
                ],         // 数据源
                showCheckbox: true,   //是否显示复选框

                multiSelect: false,    //多选
                onNodeChecked: function (event,data) {
                    alert(data.nodeId);
                },
                onNodeSelected: function (event, data) {
                    alert(data.nodeId);
                }
            });
        },
        error: function () {
            alert("树形结构加载失败！")
        }
    });
})
