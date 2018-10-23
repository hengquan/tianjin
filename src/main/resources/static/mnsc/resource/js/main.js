/**
 * Created by ciroMa on 2018/8/31.
 */
$(function () {
    var submitJson = {},
        codeStr = '获取验证码',
        codeMax = 60,
        timer = null,
        nowIdx = 0;

    /****************************************************************
     * 接口调用部分部分
     */
    // 最终提交数据
    function finalSubmit() {
        layer.alert(JSON.stringify(submitJson), {
            title: '提交数据',
            icon: 1,
            skin: 'layer-ext-moon'
        })
        // 最终提交的数据 为  submitJson
    }


    // 点击获取验证码
    $('#form-input-code').click(function () {
        console.log(timer)
        if (timer) {
            return
        }
        // 调用获取验证码接口


        $(this).html(codeMax + 's').addClass('default')
        timer = setInterval(function () {
            if (codeMax > 0){
                codeMax--
                $('#form-input-code').addClass('default').html(codeMax + 's')
            } else {
                codeMax = 60
                $('#form-input-code').html(codeStr).removeClass('default')
                clearInterval(timer)
                timer = null
            }
        }, 1000)
    })
    /****************************************************************
     * 交互操作部分
     */
    // 上一步
    $('.goBack').click(function () {
        nowIdx --
        tabChange(nowIdx)
    })


    /****************************************************************
     * 格式校验部分
     */
    // 表单1 校验
    $('#form-submit1').click(function () {
        // 格式校验
        if (!checkForm1()){
            return
        }
        nowIdx = 1
        $('#page-form-baseinfo input').each(function (idx, el) {
            submitJson[$(el).attr('name')] = $(el).val()
        })
        tabChange(nowIdx)
        toStep3()

    })
    // 表单2 校验
    $('#form-submit2').click(function () {
        // 格式校验
        if (!checkForm2()){
            return
        }
        nowIdx = 2
        $('#page-form-business input').each(function (idx, el) {
            submitJson[$(el).attr('name')] = $(el).val()
        })
        tabChange(nowIdx)
        toStep4()
    })
    // 表单3 校验
    $('#form-submit3').click(function () {
        // 格式校验
        if (!checkForm3()){
            return
        }
        nowIdx = 2
        $('#page-form-bank input').each(function (idx, el) {
            submitJson[$(el).attr('name')] = $(el).val()
        })
        finalSubmit()
        toStep5()
    })
    // form1 格式校验
    function checkForm1 () {
        var type = true
        var Layered = false
        $('#page-form-baseinfo input').each(function (idx, el) {
            var val = $(el).val()
            var title = $(el).parent().parent().find('.form-label').html()

            var returnVal = checkItem(type, el, Layered, val,title)
            type = returnVal.type
            Layered = returnVal.Layered
        })
        if ($('#basePwd').val() !== $('#basePwdConfirm').val()){

            console.log('Layered', Layered)
            if (!Layered) {
                layer.alert('两次密码不一致', {
                    title: '格式错误',
                    icon: 0,
                    skin: 'layer-ext-moon'
                })
            }
            type = false
            Layered = true
            $('#basePwd').addClass('error')
            $('#basePwdConfirm').addClass('error')
        } else if (($('#basePwd').val() === $('#basePwdConfirm').val()) && ($('#basePwdConfirm').val() !== '')) {
            $('#basePwd').removeClass('error')
            $('#basePwdConfirm').removeClass('error')
        }
        return type
    }
    // form2 格式校验
    function checkForm2 () {
        var type = true
        var Layered = false
        $('#page-form-business input').each(function (idx, el) {
            var val = $(el).val()
            var title = $(el).parent().parent().find('.form-label').html()
            var returnVal = checkItem(type, el, Layered, val,title)
            type = returnVal.type
            Layered = returnVal.Layered
        })
        return type
    }

    // form3 格式校验
    function checkForm3 () {
        var type = true
        var Layered = false
        $('#page-form-bank input').each(function (idx, el) {
            var val = $(el).val()
            var title = $(el).parent().parent().find('.form-label').html()
            var returnVal = checkItem(type, el, Layered, val,title)
            type = returnVal.type
            Layered = returnVal.Layered
        })
        return type
    }

    // 标签页切换
    function tabChange(nowIdx) {
        $('.page-steps .page-step-cell').each(function (idx, el) {
            if (idx === nowIdx){
                $(el).addClass('active')
            } else {
                $(el).removeClass('active')
            }
        })
        $('.page-form-list .page-form').each(function (idx, el) {
            if (idx === nowIdx){
                $(el).addClass('active')
            } else {
                $(el).removeClass('active')
            }
        })
    }

    // 单项表单校验
    function checkItem(type, el, Layered) {
        var val = $(el).val()
        var title = $(el).parent().parent().find('.form-label').html()
        var Layered = Layered
        var type = type
        var dateNot = $(el).data('not')
        var typeCk = $(el).data('type')

        switch (typeCk){
            case 'phone':
                if (!isPhone(val)){
                    if (!Layered) {
                        layer.alert(title +'格式错误', {
                            title: '格式错误',
                            icon: 0,
                            skin: 'layer-ext-moon'
                        })
                    }
                    type = false
                    Layered = true
                    $(el).addClass('error')
                } else{
                    $(el).removeClass('error')
                }
                break;
            case 'IDNumber':
                if (!isIDNumber(val)){
                    if (!Layered) {
                        layer.alert(title +'格式错误', {
                            title: '格式错误',
                            icon: 0,
                            skin: 'layer-ext-moon'
                        })
                    }
                    type = false
                    Layered = true
                    $(el).addClass('error')
                } else{
                    $(el).removeClass('error')
                }
                break;
            case 'retuired':
                if (val === ''){
                    if (!Layered) {
                        layer.alert(title +'不能为空', {
                            title: '格式错误',
                            icon: 0,
                            skin: 'layer-ext-moon'
                        })
                    }
                    type = false
                    Layered = true
                    $(el).addClass('error')
                } else{
                    $(el).removeClass('error')
                }
                break;
        }
        if ( dateNot && (dateNot=== val)){
            var title = $(el).parent().parent().parent().find('.form-label').html()
            if (!Layered) {
                layer.alert(title +'不能为空', {
                    title: '格式错误',
                    icon: 0,
                    skin: 'layer-ext-moon'
                })
            }
            type = false
            Layered = true
            $(el).parent().addClass('error')
        } else if (dateNot && (dateNot !== val)) {
            $(el).parent().removeClass('error')
        }
        return {
            type: type,
            Layered: Layered
        }
    }
    // 校验手机号格式
    function isPhone(str) {
        var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
        if (!myreg.test(str)) {
            return false;
        } else {
            return true;
        }
    }
    // 校验身份证格式
    function isIDNumber(str) {
        var myreg= /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        if (!myreg.test(str)) {
            return false;
        } else {
            return true;
        }
    }
    /****************************************************************
     * DOM 模拟
     */

    // 模拟下拉选项框
    mockSelectList()
    function mockSelectList() {
        $('[name="nice-select"]').click(function(e){
            $('[name="nice-select"]').find('ul').hide();
            $(this).find('ul').show();
            e.stopPropagation();
        });

        $('[name="nice-select"] li').hover(function(e){
            $(this).toggleClass('on');
            e.stopPropagation();
        });

        $('[name="nice-select"] li').click(function(e){
            var val = $(this).text();
            $(this).parents('[name="nice-select"]').find('input').val(val);
            $('[name="nice-select"] ul').hide();
            e.stopPropagation();
        });
        $(document).click(function(){
            $('[name="nice-select"] ul').hide();
        });
    }




})
//重新练习
function repractice(){
  window.location.href ="loading.html"
}
function toStep3(){
  window.location.href ="step3.html"
}

function toStep4(){
  window.location.href ="step4.html"
}
function toStep5(){
  window.location.href ="step5.html"
}
