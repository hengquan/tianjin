/**************************
 * 公共变量
 */
var comVars = {
  winHeight: ''
}
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
      var hasChild = $(el).find('.pt-menu-child').length > 0
      var clickType = $(el).hasClass('pt-menu-open')
      if (clickType && hasChild) {
        closeMenu($(this))
      } else {
        chooseMenu(idx, hasChild)
      }
    })
  });
  $('.pt-menu-child').click(function (e) {
    resetAllSelected()
    $(this).addClass('pt-menu-selected').siblings().removeClass('pt-menu-selected')
    $(this).parent('.pt-second-menu').parent('.pt-menu-list').addClass('pt-menu-open')
    return false
  })
  function chooseMenu (idx, hasChild) {
    $('.pt-menu-list').each(function (idxChild, elChild) {
      if (idx === idxChild) {
        if (hasChild) {
          $(elChild).addClass('pt-menu-open')
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
      $(elChild).removeClass('pt-menu-open pt-menu-selected')
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
    winHeight: $(window).height()
  }
  $('.pt-view').css('height', comVars.winHeight - 60);
}
function initOnce() {
  if (window.localStorage.getItem('menuOpen') === 'true') {
      openAside()
  } else {
      closeAside()
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


//点击重新开始跳转到模拟实操页
function repractice() {
  window.location.href ='loading.html'
}