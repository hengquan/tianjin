/*******************
 * 参数列表
 * el: 绑定的根结点ID
 * X: X轴坐标
 * Y: Y轴坐标
 * direction: 箭头指向 top/right/bottom/left
 * content: 包含内容(html)
 * padding: 填充值
 * backgroundColor：背景颜色
 * color：字体颜色
 * radius：圆角
 * area：[宽， 高]
 * nextBtn：按钮
 *     nextBtn.content 内容文字
 *     nextBtn.backgroundColor 按钮背景颜色
 *
 *
 */
(function (window) {

    var IntroduceTipsArr = [],
        IntroduceTipsCurrent = -1,
        $el = '';
    var IntroduceTipsList = function (obj) {
        IntroduceTipsArr = obj.list
        $el = obj.el
        IntroduceTipsCurrent = -1
        IntroduceTipsListNext()
    }
    function IntroduceTipsListNext () {
        IntroduceTipsCurrent ++
        if (IntroduceTipsArr.length <= IntroduceTipsCurrent){
            new IntroduceTips().cleanOld()
        } else {
            new IntroduceTips(IntroduceTipsArr[IntroduceTipsCurrent], IntroduceTipsListNext)
        }
    }

    /*******************
     * 创建tips
     */
    function IntroduceTips (args, callBack) {
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
        this.init(args)
    }
    IntroduceTips.prototype.init = function (args) {
        this.cleanOld()
        this.initOpts(args)
        this.showTipsInit()

    }
    // 初始化全局参数
    IntroduceTips.prototype.initOpts = function (args) {
        $.extend(this.opt, args)
        this.opt.el = $el
        this.$root = $(this.opt.el) // 跟节点
        this.$rootWidth = this.$root.outerWidth()
        this.$rootHeight = this.$root.outerHeight()
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
        var $btn = $('<div class="tips-btn" >'+this.opt.nextBtn.content+'</div>')

        $btn.on("click",function(){
            if (_this.callBack instanceof Function){
                _this.callBack()
            } else {
                _this.cleanOld()
            }
        });
        $TipsArrow.addClass(this.opt.direction)
        this.initArrowCss($TipsArrow)
        $TipsDom.append($TipsContext)
        $TipsDom.append($TipsArrow)
        $TipsDom.append($btn)
        this.$dom = $TipsDom
    }
    // 初始化dom位置
    IntroduceTips.prototype.initPosition = function () {
        var left,
            top;
        switch (this.opt.direction) {
            case 'top':
                left = this.opt.X  - this.opt.area[0] / 2 + this.arrowWidth
                top = this.opt.Y - this.opt.area[1] - this.arrowWidth * 2 -  this.opt.distance
                break;
            case 'right':
                left = this.opt.X + this.arrowWidth * 2 + this.opt.distance
                top = this.opt.Y - this.opt.area[1] / 2 + this.arrowWidth
                break;
            case 'bottom':
                left = this.opt.X  - this.opt.area[0] / 2 - this.arrowWidth
                top = this.opt.Y + this.arrowWidth * 2 + this.opt.distance
                break;
            case 'left':
                left = this.opt.X - this.opt.area[0] - this.arrowWidth * 2 - this.opt.distance
                top = this.opt.Y - this.opt.area[1] / 2 - this.arrowWidth
                break;
        }

        if (left < 0){
            left  = 0
        } else if (left + this.opt.area[0] + this.arrowWidth * 2 > this.$rootWidth){
            left = this.$rootWidth - this.opt.area[0] - this.arrowWidth * 2
        }
        if (top < 0){
            top  = 0
        } else if (top + this.opt.area[1] > this.$rootHeight){
            top = this.$rootHeight - this.opt.area[1]
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
})(window)

