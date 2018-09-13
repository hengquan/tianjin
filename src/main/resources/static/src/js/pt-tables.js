$(function () {
    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

});

    // 2.返回操作模版
function operateFormatter(value, row, index) {
    return [
        '<button type="button" class="RoleOfdelete btn btn-xs btn-default btn-link" onclick="deleteItem('+index+')">删除</button>',
        '<button type="button" class="RoleOfedit btn btn-xs btn-default btn-link" onclick="editItem('+index+')">修改</button>',
        '<button type="button" class="RoleOflook btn btn-xs btn-default btn-link" onclick="lookItem('+index+')">查看</button>'
    ].join('');
}
// 3.根据返回值编辑显示内容
function operateState(value, row, index) {
    if (value === '停用') {
        return '<span class="pt-warn">'+value+'</span>'
    }
    if (value === '已启用') {
        return '<span class="pt-success">'+value+'</span>'
    }
    return value
}



var TableInit = function (queryParam) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_departments').bootstrapTable({
            url: 'http://mazhaoyang.cn/index.php/home/output/GangkouTable',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: false,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: queryParam,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            showColumns:false,
            columns: [
                [
                    // 设置一行
                    {
                        title: '标题',
                        align: "center",
                        valign: 'middle',
                        colspan: 10
                    }
                ],
                [
                    {
                        field: 'rowSpan',
                        title: '列合并标题1',
                        align: "center",
                        valign: 'middle',
                        colspan:1,
                        rowspan:2
                    },
                    {
                        title: '行合并标题2',
                        align: "center",
                        valign: 'middle',
                        colspan: 5,     // colspan控制多行合并
                        rowspan:1,
                    },
                    {
                        title: '行合并标题3',
                        align: "center",
                        valign: 'middle',
                        colspan: 4,
                        rowspan:1,
                    },
                ],

                [
                    {
                        checkbox: true
                    },{
                        field: 'index',
                        title: '序号'
                    },{
                        field: 'name',
                        title: '费用名称'
                    },{
                        field: 'pricingForm',
                        title: '定价形式',
                    },{
                        field: 'ClassificationOfFees',
                        title: '费项分类'
                    },{
                        field: 'typeOfTrade',
                        title: '贸易类型'
                    },{
                        field: 'chargeSubject',
                        title: '收费主体'
                    },{
                        field: 'state',
                        title: '状态',
                        formatter: operateState
                    }, {
                        field: 'methods',
                        title: '操作',
                        formatter: operateFormatter
                    },
                ]
            ],
            onLoadSuccess : function(data) {
                var data = $('#tb_departments').bootstrapTable('getData', true);
                console.log(data);
                //合并单元格
                mergeCells(data, "rowSpan", 1, $('#tb_departments'));

            },
        });
    };

    //得到查询的参数
//    oTableInit.queryParams = function (params) {
//        console.log(params);
//        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
//            limit: params.limit,   //页面大小
//            offset: params.offset,  //页码
//        };
//        return temp;
//    };
    return oTableInit;
};

/**
 * 合并单元格
 * @param data  原始数据（在服务端完成排序）
 * @param fieldName 合并属性名称
 * @param colspan   合并列
 * @param target    目标表格对象
 */
function mergeCells(data,fieldName,colspan,target){
    //声明一个map计算相同属性值在data对象出现的次数和
    var sortMap = {};
    for(var i = 0 ; i < data.length ; i++){
        for(var prop in data[i]){
            if(prop == fieldName){
                var key = data[i][prop]
                if(sortMap.hasOwnProperty(key)){
                    sortMap[key] = sortMap[key] * 1 + 1;
                } else {
                    sortMap[key] = 1;
                }
                break;
            }
        }
    }
    for(var prop in sortMap){
        console.log(prop,sortMap[prop])
    }
    var index = 0;
    for(var prop in sortMap){
        var count = sortMap[prop] * 1;
        $(target).bootstrapTable('mergeCells',{index:index, field:fieldName, colspan: colspan, rowspan: count});
        index += count;
    }
}

// 操作模版调用的方法
function deleteItem(e) {
    alert('删除操作，序号' + e)
}

function editItem(e) {
    layer.open({
        type: 2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
        title: '<span class="text-color">新增费项信息</span>',//请修改span中间的内容
        shadeClose: true,//开启阴影关闭
        closeBtn: 1, //不显示关闭按钮
        shade: 0.6,
        maxmin: false,//开启最大化最小化按钮
        area: ['62.5%', '83%'],//此处可以改为100%，该数值按UI图比例算出
        content: 'content.html'//如果不想出现滚动条['content.html', 'no']
    });
}

function lookItem(e) {
    layer.open({
        type: 2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
        title: '<span class="text-color">查看费项信息</span>',//请修改span中间的内容
        shadeClose: true,//开启阴影关闭
        closeBtn: 1, //不显示关闭按钮
        shade: 0.6,
        maxmin: false,//开启最大化最小化按钮
        area: ['62.5%', '83%'],//此处可以改为100%，该数值按UI图比例算出
        content: 'look.html'//如果不想出现滚动条['content.html', 'no']
    });
}
$.fn.bootstrapTable.locales['zh-CN'] = {
    formatLoadingMessage: function () {
        return '正在努力地加载数据中，请稍候……';
    },
    formatRecordsPerPage: function (pageNumber) {
        return '每页显示 ' + pageNumber + ' 条记录';
    },
    formatShowingRows: function (pageFrom, pageTo, totalRows) {
        return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，总共 ' + totalRows + ' 条记录';
    },
    formatSearch: function () {
        return '搜索';
    },
    formatNoMatches: function () {
        return '没有找到匹配的记录';
    },
    formatPaginationSwitch: function () {
        return '隐藏/显示分页';
    },
    formatRefresh: function () {
        return '刷新';
    },
    formatToggle: function () {
        return '切换';
    },
    formatColumns: function () {
        return '列';
    },
    formatExport: function () {
        return '导出数据';
    },
    formatClearFilters: function () {
        return '清空过滤';
    }
};

$.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN']);