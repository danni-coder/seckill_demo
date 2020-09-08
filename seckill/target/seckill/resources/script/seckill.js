//存放主要交互逻辑JS代码
//javaScript  模块化
//seckill.detail.init(params)
var seckill = {
    //封装描述相关ajax的url
    URL:{
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution : function (seckillId,md5) {
            return '/seckill/'+ seckillId + '/' + md5 + '/excute';
        },
    },
    //处理秒杀逻辑
    handleSeckillKill:function(seckillId, node){
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');//新增秒杀按钮  默认隐藏
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回调函数中执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['expesed']){
                    //秒杀已开启
                    //获取描述地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);

                    //使用.one实现按钮一次绑定
                    $('#killBtn').one('click',function () {
                        //执行秒杀请求
                        //1:先禁用按钮
                        $(this).addClass('disabled');
                        //2:发送秒杀请求执行秒杀
                        $.post(killUrl,{},function (result) {
                            console.log("result:" + result.toString());
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3:显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }else{
                                console.log("result:" + result.toString());
                            }
                        })
                    });
                    node.show();
                }else{
                    //秒杀未开启
                    var now = exposer['now'];
                    var startTime = exposer['start'];
                    var endTime = exposer['end'];
                    //重新进入计时逻辑
                    seckill.countdown(seckillId,now,startTime,endTime);
                }
            }else{
                console.log('result='+result);
            }
        })
    },
    //验证手机号
    validatePhone: function(phone){
        //如果没有 则是undefined  undefined就是false
        if(phone && phone.length == 11 && !isNaN(phone)){
            //已经登录有手机号
            return true;
        }else{
            return false;
        }
    },
    countdown: function(seckillId,nowTime,startTime,endTime){
        //描述结束
        var seckillBox = $('#seckill-box');
        if(nowTime > endTime) {
            seckillBox.html('秒杀结束！');
        }else if(nowTime < startTime){
            //秒杀未开始  计时时间绑定
            var killTime = new Date(startTime + 1000);//加上1秒  防止客户端计时偏移
            seckillBox.countdown(killTime,function (event) {
                //回调函数event  控制时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //时间完成后回调时间
            }).on('finish.countdown',function () {
                //获取描述地址，控制显示逻辑 执行秒杀
                seckill.handleSeckillKill(seckillId,seckillBox);
            });
        }else {
            //已经开始的
            seckill.handleSeckillKill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录 + 计时交互
            //规划我们的交互流程
            //1:数据验证
            //在cookie中查找手机号
            var killPhome = $.cookie('killPhone');
            //从参数param中拿到入参值
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //验证手机号
            if (!seckill.validatePhone(killPhome)) {
                //绑定手机号
                //获取弹出层
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show : true,//显示弹出层
                    backdrop : 'static',//指定一个静态的背景，当用户点击模态框外部时不会关闭模态框
                    keyboard : false //关闭键盘事件
                })
                //点击手机号提交时间
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    //用户输入有效的手机号
                    if(seckill.validatePhone(inputPhone)){
                        //电话写入cookie  设计过期时间1天
                        $.cookie('killPhone',inputPhone,{expires:1,path:'/seckill'});
                        //直接刷新页面
                        window.location.reload();
                    }else{
                        $('#killPhoneMessage').hide().html('<label class = "label label-danger">手机号错误！</label>').show(300)
                    }
                })
            }
            //已经登录
            //2:主要是计时交互
            //获取系统当前时间
            $.get(seckill.URL.now(), {},function (result) {
                //判断返回
                if(result && result['success']){
                   var nowTime = result['data'];
                   //调用计时方法
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                }else{
                    //未获取到数据或者不成功，则打印相应返回
                    console.log("result:" + result);
                }
            })
        }
    }
}
