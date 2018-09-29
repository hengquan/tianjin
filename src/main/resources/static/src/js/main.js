/**************************
 * 公共变量
 */
var comVars = {
  winHeight: '',
};
/**
 * 临时变量
 * @type {{}}
 */
var temp = {
};

$(function () {
  /**************************
   * 初始化
   */
  initDom()
  initOnce()
  $(window).resize(function () {
    initDom()
  })
  /**************************
   * 菜单栏控制
   */
  $('.pt-menu-list').each(function (idx, el) {
    $(this).click(function () {
      $(this).siblings().children().find('.pt-menu-child').removeClass('pt-child-s');
      var hasChild = $(el).find('.pt-menu-child').length > 0;
      var clickType = $(el).hasClass('pt-menu-open');
      if (clickType && hasChild) {
        closeMenu($(this))
      } else {
        chooseMenu(idx, hasChild)
      }
    })
  });

  $('.pt-menu-child').click(function (e) {
    resetAllSelected();
    var frameUrl = $(e.currentTarget).data('url');
    if(frameUrl) {
      changeMainframeSrc(frameUrl);
    }
    $(this).addClass('pt-child-s').siblings().removeClass('pt-child-s');
    $(this).addClass('pt-child-s').parents('.pt-menu-list').siblings().children().find('.pt-menu-child').removeClass('pt-child-s');
    $(this).parent('.pt-second-menu').parent('.pt-menu-list').addClass('pt-menu-open pt-menu-selected')
    return false
  });
  $('.pt-menu-tourl').click(function (e) {
    resetAllSelected();
    var frameUrl = $(e.currentTarget).data('url');
    if(frameUrl) {
      changeMainframeSrc(frameUrl);
    }
    $(e.currentTarget).addClass('pt-menu-open');
    $(e.currentTarget).addClass('pt-menu-selected');
    // $(this).addClass('pt-child-s').siblings().removeClass('pt-child-s')
    // $(this).addClass('pt-child-s').parents('.pt-menu-list').siblings().children().find('.pt-menu-child').removeClass('pt-child-s')
    // $(this).parent('.pt-second-menu').parent('.pt-menu-list').addClass('pt-menu-open pt-menu-selected')
    return false
  });

  function chooseMenu (idx, hasChild) {
    $('.pt-menu-list').each(function (idxChild, elChild) {
      if (idx === idxChild) {
          if (hasChild) {
              // $(elChild).addClass('pt-menu-open')
              resetAllSelected()
              $(elChild).addClass('pt-menu-open')
              $(elChild).addClass('pt-menu-selected')
          } else {
              resetAllSelected()
              $(elChild).addClass('pt-menu-selected')
          }
      } else {
        if (hasChild) {
          $(elChild).removeClass('pt-menu-open')
        } else {
          $(elChild).removeClass('pt-menu-selected')
        }
      }
    })
  }
  function closeMenu (obj) {
    obj.removeClass('pt-menu-open')
  }
  function resetAllSelected () {
    $('.pt-menu-list').each(function (idxChild, elChild) {
      $(elChild).removeClass('pt-menu-open pt-menu-selected');
      if ($(elChild).find('.pt-menu-child').length > 0) {
        $(elChild).find('.pt-menu-child').each(function (i, e) {
          $(e).removeClass('pt-menu-selected')
        })
      }
    })
  }

})

/**************************
 * DOM 控制
 */
function initDom () {
  comVars = {
    winHeight: $(window).height(),
    mainframe: function() {
      return document.getElementById("mainframe");
    }
  };
  $('.pt-view').css('height', comVars.winHeight - 60);
  var leftbarH = comVars.winHeight - 120;
  $(".pt-menu").css("height", leftbarH);
}
function initOnce() {
  if (window.localStorage.getItem('menuOpen') === 'true') {
  } else {
  }
  $('#pt-bar').click(function () {
      console.log($('.pt-nav').hasClass('pt-open'))
      if ($('.pt-nav').hasClass('pt-open')){
          closeAside()
      } else {
          openAside()
      }
  })

}
function closeAside() {
  $('.pt-nav').removeClass('pt-open');
  $('.pt-view').removeClass('pt-open');
  localStorage.setItem('menuOpen','false')
}
function openAside() {
  $('.pt-nav').addClass('pt-open');
  $('.pt-view').addClass('pt-open');
  localStorage.setItem('menuOpen','true')
}
function loadNicescroll() {
  var allH = $(window).height();
  var leftbarH = allH - 120;
  $(".pt-menu").css("height", leftbarH);
  $(".pt-menu").niceScroll({
    cursorborder: "",
    cursorcolor: "#6598DE",
    boxzoom: true,
    autohidemode: 'cursor'
  });
  $(".pt-menu").getNiceScroll().hide();

}

/**
 * ======================================
 * top页面控制mainframe,以及调用mainframe中的方法
 */

/**
 * 刷新mainframe中的表格
 * @param id 表格dom的id
 */
function refreshTable(id) {
  comVars.mainframe().contentWindow.AF.refreshTable(id);
}

/**
 * 更改mainframe的src
 */
function changeMainframeSrc(src) {
  comVars.mainframe().src = src;
}

function changeRole(e) {
  window.location.href = '?roleName=' + e.textContent
}

/**
 * 获得参数
 */
function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
  var r = window.location.search.substr(1).match(reg); //匹配目标参数
  if (r != null) return unescape(r[2]); return null; //返回参数值
}