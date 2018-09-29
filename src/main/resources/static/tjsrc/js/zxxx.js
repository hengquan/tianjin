$(function() {
  $(".tag-box").on("click","li",function() {
    $(this).addClass("on").siblings("li").removeClass("on")
  })
  $.ajax({
    type: "get",
    url: "../../demoJson/json111.json",
    data: "",
    dataType: "json",
    success: function (res) {
      console.log(res)
      var arr = []
      var html = ""
      for(var i = 0;i<res.data.list.length;i++) {
        if(res.data.list[i].parentId == 0) {
          arr.push(res.data.list[i])
        }
      }
      for(var j=0;j<arr.length;j++) {
        html+= '<li>'+arr[j].name+'</li>'
      }
      $(".tag-box").append(html);
    }
  });
})