

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (url,columns) {
        $('#tb_departments').bootstrapTable({
            url: url,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: false,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            columns: columns
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        alert(params.sort + ',' + params.order)
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            sortName: params.sort,
            sortType: params.order
        };
        return temp;
    };
    return oTableInit;
};

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