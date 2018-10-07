$(function() {
    $.ajax({
        type: "get",
        url: "../../demoJson/json131.json",
        data: "",
        dataType: "json",
        success: function (res) {
            console.log(res)
            if(res.returnCode == 00) {
                var html = ""
                for(var i = 0;i < res.data.length; i++) {
                    html += '<li class="col-sm-3">'
                        +'<div class="con">'
                        +'<div class="pic"><img src="'+(res.data[i].mainUrl || "../../tjsrc/images/mnsc_default.jpg")+'"></div>'
                        +'<div class="botoom">'
                            +'<h2>'+res.data[i].mnscName+'</h2>'
                            +'<p>'+res.data[i].remarks+'</p>'
                            +'<div type="button" class="btns">开始模拟实操</div>'
                        +'</div>'
                        +'</div>'
                    +'</li>'
                }
                $(".listbox").append(html)
            }
        }
    });
})