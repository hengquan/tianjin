/*******************
 * 参数列表
 *
 * el: 绑定的根结点ID
 *
 * nav: 步骤菜单配置
 * nav.el 步骤菜单绑定节点ID
 * nav.list array，步骤菜单内容文字
 *
 * iframe frame配置
 * iframe.el frame绑定节点ID
 * iframe.width frame宽
 * iframe.height frame高
 *
 * list 列表数据
 * list.srcUrl 对应iframe的链接
 * list.steps 步骤列表
 *
 * X: X轴坐标
 * Y: Y轴坐标
 * direction: 箭头指向 top/right/bottom/left
 * content: 包含内容(html)
 * padding: 填充值
 * backgroundColor：背景颜色
    noTriangle: 不要三角
    borderColor: 边框颜色
 * color：字体颜色
 * radius：圆角
 * area：[宽， 高]
 * nextBtn：按钮
 *     nextBtn.content 内容文字
 *     nextBtn.backgroundColor 按钮背景颜色
 * callBack 执行后的回调函数
 *
 *
 * 主动控制方法
 * 上一步 new IntroduceControl().prevPage()
 * 下一步 new IntroduceControl().nextPage()
 * 重新开始本步骤 new IntroduceControl().rePage()
 *
 *
 *
 */

(function (window) {
    var rootObj = {},
        IntroduceTipsArr = [],
        IntroduceTipsCurrent = 0,
        IntroduceTipsStep = -1,
        btnCallBackPrev = '',
        $el = '';
    var IntroduceTipsList = function (obj) {
        rootObj = obj
        IntroduceTipsArr = obj.list
        $el = obj.el
        IntroduceTipsCurrent = 0
        IntroduceTipsListNext()
    }
    function IntroduceTipsListNext () {
        var iframe = document.getElementById('tipsMain')
        iframe.onload = function() {
            //解决打开高度太高的页面后再打开高度较小页面滚动条不收缩
            var imgs=iframe.contentWindow.document.getElementsByTagName("img");
            iframe.height = imgs[0].height+15
        }
        if (!IntroduceTipsArr[IntroduceTipsCurrent]){
            new IntroduceControl().cleanOld()
            return
        }
        var targetObj = {}
        if (IntroduceTipsArr[IntroduceTipsCurrent].steps.length - 1 > IntroduceTipsStep){
            IntroduceTipsStep ++
            targetObj = IntroduceTipsArr[IntroduceTipsCurrent].steps[IntroduceTipsStep]
            if (targetObj.callBack && targetObj.callBack instanceof Function){
                targetObj.callBack({list: IntroduceTipsCurrent, step: IntroduceTipsStep})
            }

            new IntroduceTips(targetObj, IntroduceTipsListNext)
        } else {
            new IntroduceControl().cleanOld()
        }
        if (IntroduceTipsArr[IntroduceTipsCurrent].steps.length === 0){
            IntroduceTipsStep = 0
            new IntroduceTips({}).changeFrame()
        }
        new IntroStepMenu().init()
    }

    /*****************
     * 跳转控制
     */
    function IntroduceControl() {
    }
    IntroduceControl.prototype.nextPage = function () {
        IntroduceTipsCurrent ++
        IntroduceTipsStep = -1
        console.log(IntroduceTipsCurrent)
        IntroduceTipsListNext()
    }
    IntroduceControl.prototype.prevPage = function () {
        IntroduceTipsCurrent --
        IntroduceTipsStep = -1
        IntroduceTipsListNext()
    }
    IntroduceControl.prototype.restPage = function (index) {
      IntroduceTipsCurrent = index
      IntroduceTipsStep = -1
      IntroduceTipsListNext()
    }
    IntroduceControl.prototype.rePage = function () {
        IntroduceTipsCurrent =0
        IntroduceTipsStep = -1
        IntroduceTipsListNext()
    }
    IntroduceControl.prototype.cleanOld = function () {
        $el = $(rootObj.el)
        $($el).find('.tips-container').remove()
    }


    /*******************
     * 创建步骤菜单
     */
    function IntroStepMenu () {

    }
    IntroStepMenu.prototype.init = function () {
        this.createDom()
    }
    // 创建DOM
    IntroStepMenu.prototype.createDom = function () {
        var $innerHtml = '<ul class="pro">',
            $navRoot = $(rootObj.nav.el),
            _list = rootObj.nav.list;
        $navRoot.html('')
        for(var i = 0; i< _list.length; i++){

            var moreClass = ''
            if (i === 0){
                moreClass += 'frist '
            }
            if (i === (_list.length-1)){
                moreClass += 'last '
            }

            if (IntroduceTipsCurrent === i){
                moreClass += 'intro-current'
            }
            if (IntroduceTipsCurrent > i){
                moreClass += 'intro-used'
            }

            $innerHtml += '<li class="  '+moreClass+'"><span>'+(i+1)+'</span>'+_list[i]+'</li>'
        }
        $innerHtml += '</ul>'
        $navRoot.append($innerHtml)
    }



    /*******************
     * 创建tips
     */
    function IntroduceTips (args, callBack) {
        if (JSON.stringify(args) === '{}'){
            return
        }
        this.callBack = callBack
        this.opt = {
            el: '#introduce-container',
            X: 250,
            Y: 350,
            distance: 10,
            direction: 'top',
            content: '<div>包含内容</div>',
            padding: 10,
            backgroundColor: 'rgba(0,0,0,0.6)',
            color: '#fff',
            radius: 5,
            area: [100, 100],
            nextBtn: {
                content: '下一步',
                backgroundColor: 'blue'
            }
        }
        this.changeFrame()
        this.initOpts(args)
        this.init(args)
    }
    IntroduceTips.prototype.changeFrame = function () {
        if (IntroduceTipsStep === 0 && IntroduceTipsArr[IntroduceTipsCurrent] && IntroduceTipsArr[IntroduceTipsCurrent].srcUrl){
            var $frame = $(rootObj.iframe.el)
            $frame.attr('src', IntroduceTipsArr[IntroduceTipsCurrent].srcUrl)
            $frame.attr('height', rootObj.iframe.height)
            $frame.attr('width', rootObj.iframe.width)
        }
    }
    IntroduceTips.prototype.init = function () {
        new IntroduceControl().cleanOld()
        this.showTipsInit()
    }
    // 初始化全局参数
    IntroduceTips.prototype.initOpts = function (args) {
        $.extend(this.opt, args)
        this.opt.el = $el
        this.$root = $(this.opt.el) // 跟节点
        this.$rootWidth = this.$root.outerWidth()
        this.$rootHeight = this.$root.outerHeight()
        this.$rootY = this.$root.offset().top
        this.$rootX = this.$root.offset().left
        this.$dom = ''  // 要插入的dom节点
        this.arrowWidth = 10
    }
    /**********************************************
     * 清除之前的tips
     */
    IntroduceTips.prototype.cleanOld = function () {
        $($el).find('.tips-container').remove()
    }
    /**********************************************
     * 创建tips
     */
    IntroduceTips.prototype.showTipsInit = function () {
        this.createTipsDom()
        this.initCss()
        this.initPosition()
        this.insertRoot()
    }
    // 创建dom节点
    IntroduceTips.prototype.createTipsDom = function () {
        var _this = this
        var $TipsDom = $('<div class="tips-container">')
        var $TipsContext = this.opt.content
        var $TipsArrow = $('<div class="tips-arrow">')
        var $btn = $('<div class="tips-btn" '+(this.opt.nextBtn.backgroundColor=='transparent'?' style="background:transparent" ':'')+'>'+this.opt.nextBtn.content+'</div>')

        $btn.on("click",function(){
            btnCallBackPrev = _this.opt.nextBtn.callBack
            if (btnCallBackPrev instanceof Function){
                btnCallBackPrev()
            } else {
                if (_this.callBack instanceof Function){
                    _this.callBack()
                } else {
                    new IntroduceControl().cleanOld()
                }
            }
        });
        $TipsArrow.addClass(this.opt.direction)
        this.initArrowCss($TipsArrow)
        $TipsDom.append($TipsContext)
        if (this.opt.noTriangle !== true){
            $TipsDom.append($TipsArrow)
        }
        if (this.opt.nextBtn.noBtn !== true){
            $TipsDom.append($btn)
        }
        this.$dom = $TipsDom
    }
    // 初始化dom位置
    IntroduceTips.prototype.initPosition = function () {
        var left, top;
        var joinX = this.$rootX + this.opt.X,
            joinY = this.$rootY + this.opt.Y;
        switch (this.opt.direction) {
            case 'top':
                left = joinX - this.opt.area[0] / 2 + this.arrowWidth
                top = joinY - this.opt.area[1] - this.arrowWidth * 2 -  this.opt.distance
                break;
            case 'right':
                left = joinX + this.arrowWidth * 2 + this.opt.distance
                top = joinY - this.opt.area[1] / 2 + this.arrowWidth
                break;
            case 'bottom':
                left = joinX  - this.opt.area[0] / 2 - this.arrowWidth
                top = joinY + this.arrowWidth * 2 + this.opt.distance
                break;
            case 'left':
                left = joinX - this.opt.area[0] - this.arrowWidth * 2 - this.opt.distance
                top = joinY - this.opt.area[1] / 2 - this.arrowWidth
                break;
        }

        this.$dom.css({
            'left': left,
            'top': top,
        })
    }
    // 初始化dom样式
    IntroduceTips.prototype.initCss = function () {
        this.$dom.css({
            'display': 'inline-block',
            'zIndex': 10,
            'position': 'absolute',
            'padding': this.opt.padding,
            'backgroundColor': this.opt.backgroundColor,
            'color': this.opt.color,
            'borderRadius': this.opt.radius,
            'width': this.opt.area[0],
            'height': this.opt.area[1],
            'boxSizing':'border-box'

        })
        if (this.opt.borderColor){
            this.$dom.css({
                'border': '1px solid ' + this.opt.borderColor,
            })

        }
    }
    // 设置arrow样式
    IntroduceTips.prototype.initArrowCss = function ($TipsArrow) {
        $TipsArrow.css({
            'borderWidth': this.arrowWidth,
            'borderStyle': 'solid',
            'display': 'inline-block',
            'position': 'absolute',
            'overFlow': 'auto'

        })
        switch (this.opt.direction){
            case 'top':
                $TipsArrow.css({
                    'borderLeftColor': this.opt.backgroundColor,
                    'borderTopColor': this.opt.backgroundColor,
                    'borderBottomColor': 'transparent',
                    'borderRightColor': 'transparent',
                    'bottom': '-20px',
                    'left': '50%',
                    'marginLeft': '-10px',
                })
                break;
            case 'right':
                $TipsArrow.css({
                    'borderRightColor': this.opt.backgroundColor,
                    'borderTopColor': this.opt.backgroundColor,
                    'borderBottomColor': 'transparent',
                    'borderLeftColor': 'transparent',
                    'left': '-20px',
                    'top': '50%',
                    'marginTop': '-10px',
                })
                break;
            case 'bottom':
                $TipsArrow.css({
                    'borderRightColor': this.opt.backgroundColor,
                    'borderBottomColor': this.opt.backgroundColor,
                    'borderLeftColor': 'transparent',
                    'borderTopColor': 'transparent',
                    'top': '-20px',
                    'left': '50%',
                    'marginLeft': '-10px',
                })
                break;
            case 'left':
                $TipsArrow.css({
                    'borderLeftColor': this.opt.backgroundColor,
                    'borderBottomColor': this.opt.backgroundColor,
                    'borderTopColor': 'transparent',
                    'borderRightColor': 'transparent',
                    'right': '-20px',
                    'top': '50%',
                    'marginTop': '-10px',
                })
                break;

        }

    }
    // root-dom中插入tips
    IntroduceTips.prototype.insertRoot = function () {
        this.$root.append(this.$dom)
    }

    window.IntroduceTipsList = IntroduceTipsList
    window.IntroduceControl = IntroduceControl
})(window)

