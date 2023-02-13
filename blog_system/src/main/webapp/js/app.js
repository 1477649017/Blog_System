
//因为列表页 详情页 编辑页都会要判断登录状态 所以这段代码就可以直接抽离出来
function getLoginStatus(){
    //利用这个函数，在访问列表页的时候给服务器发送一个请求判断登录状态
    $.ajax({
        type : 'get',
        url : 'login',
        success : function(body){
            //得到200 就不做任何工作
            console.log("当前已经登录过了!");
        },
        error : function(){
            //得到非2开头的状态码就会进入这里，说明登录状态有问题
            //让页面强制跳转到登录页
            console.log("当前未登录!");
            location.assign("blog_login.html");
        }
    })
}


