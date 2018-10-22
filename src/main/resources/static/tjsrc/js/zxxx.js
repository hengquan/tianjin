$(function() {
  $(".tag-box").on("click","li",function() {
    $(this).addClass("on").siblings("li").removeClass("on")
  })
  $.ajax({
    type: "get",
    url: "./train/getCateData",
    data: "",
    dataType: "json",
    success: function (res) {
      var arr = []
      var html = ""
      for(var i = 0;i<res.data.list.length;i++) {
        if(res.data.list[i].parentId == 0) {
          arr.push(res.data.list[i])
        }
      }
      for(var j=0;j<arr.length;j++) {
        html+= '<li cateId="'+arr[j].id+'">'+arr[j].name+'</li>'
      }
      $(".tag-box").append(html);
      $('.tag-box li').click(function () {
        getKjList($(this).attr("cateId"), $("#searchStr").val());
      });
    }
  });
  $(".search-btn").click(function(){
    getKjList($(this).attr("cateId"), $("#searchStr").val());
  });

//  $.ajax({
//    type: "get",
//    url: "../../demoJson/json121.json",
//    data: "",
//    dataType: "json",
//    success: function (res) {
//        console.log(res)
//        if(res.returnCode == 00) {
//            var html = ""
//            for(var i = 0;i < res.data.length; i++) {
//                html += '<li class="col-sm-3s">'
//                    +'<div class="con">'
//                    +'<div class="pic"><img src="'+(res.data[i].imgUrl || "../../tjsrc/images/mnsc_default.jpg")+'"></div>'
//                    +'<div class="botoom">'
//                        +'<h2>'+res.data[i].name+'</h2>'
//                        +'<div class="row-box">'
//                          +'<div class="left">'
//                            +'<span class="eay"><img src="../../tjsrc/images/rs.jpg"></span>'
//                            +'<span class="num">'+res.data[i].visitCount+'</span>'
//                          +'</div>'
//                          +'<div class="right"><span>'+res.data[i].createDate+'</span>发布</div>'
//                        +'</div>'
//                      +'</div>'
//                    +'</div>'
//                +'</li>'
//            }
//            $(".listbox").append(html)
//        }
//    }
//  });
  getKjList($(".tag-box .on").attr("cateId"), $("#searchStr").val());
});

function getKjList(cateId, searchStr) {
  if (!cateId) cateId="";
  if (!searchStr) searchStr="";
  var _data={
    "categoryId":cateId,
    "searchStr":searchStr,
    "pageNo":-1 //不分页
  };
  $.ajax({
    type: 'post',
    url: "./train/getKjList",
    data: _data,
    dataType:'json',
    success: function (res) {
      $(".listbox").html("")
      if(res.returnCode=='00') {
        var html = ""
        for(var i = 0;i < res.data.length; i++) {
          html += '<li class="col-sm-3s">'
              +'<div class="con" title="'+res.data[i].name+'" onclick="toCourse(\''+res.data[i].id+'\')">'
              +'<div class="pic"><img src="'+(res.data[i].imgUrl || "../../tjsrc/images/mnsc_default.jpg")+'"></div>'
              +'<div class="botoom">'
                  +'<h2>'+res.data[i].name+'</h2>'
                  +'<div class="row-box">'
                    +'<div class="left">'
                      +'<span class="eay"><img src="../../tjsrc/images/rs.jpg"></span>'
                      +'<span class="num">'+res.data[i].visitCount+'</span>'
                    +'</div>'
                    +'<div class="right"><span>'+res.data[i].createDate+'</span></div>'
                  +'</div>'
              +'</div>'
              +'</div>'
              +'</li>'
        }
        $(".listbox").append(html)
      }
    },
    error: function (e) {
      console.log("网络连接失败！");
    }
  });
}

function toCourse(id) {
  window.open("./courseContent.html?id="+id, "_blank");
}
