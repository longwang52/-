<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <title>北京二手房数据可视化系统</title>
  <link rel="shortcut icon" href="./img/favicon.ico" />
  <link href="./css/bootstrap.min.css" rel="stylesheet">
  <link href="./css/login.css" rel="stylesheet">
  <script src="./js/components/jquery-3.7.1.js"></script>
  <script src="./js/components/bootstrap.min.js"></script>
  <script src="./js/import.js"></script>

  <script>
    $(function(){
      // 初始化代码可以放在这里
    });

    // 检查输入是否为空
    function isEmpty(value) {
      return value == null || value.trim() === "";
    }

    // 点击登录事件
    function loginEvent() {
      // 清除之前的错误信息
      $("#messageTip").html("");
      $("#messageServerTip").html("");

      // 验证用户名和密码
      if(isEmpty($('#username').val())) {
        $("#messageTip").html("用户名为空");
        return;
      }
      if(isEmpty($('#userpwd').val())) {
        $("#messageTip").html("密码为空");
        return;
      }

      // 登录表单验证通过，提交表单
      $("#form").submit();
    }

    // 点击注册事件
    function registerEvent() {
      // 清除之前的错误信息
      $("#rmessageTip").html("");

      // 获取输入值
      const username = $("#rusername").val();
      const password = $("#newpwd").val();
      const confirmPwd = $("#conpwd").val();
      const gender = $("input[name='rsex']:checked").val();

      // 前端验证
      if(isEmpty(username)) {
        $("#rmessageTip").html("用户名为空");
        return;
      }
      if(isEmpty(password)) {
        $("#rmessageTip").html("密码为空");
        return;
      }
      if(password !== confirmPwd) {
        $("#rmessageTip").html("两次输入的密码不一致");
        return;
      }

      // 发送注册请求
      $.ajax({
        type: 'post',
        url: "register",
        data: {
          username: username,
          password: password,
          gender: gender
        },
        success: function (response) {
          const result = parseInt(response.register);
          const message = response.message || "";

          if(result === 1) {
            alert("注册成功: " + message);
            $("#exampleModalCenter").modal('hide');
            // 清空注册表单
            $("#rusername").val('');
            $("#newpwd").val('');
            $("#conpwd").val('');
          } else {
            $("#rmessageTip").html("注册失败: " + message);
          }
        },
        error: function () {
          $("#rmessageTip").html("网络错误，请稍后重试");
        }
      });
    }
  </script>
</head>
<body>
<div id="main">
  <div class="container wrap">
    <div class="row justify-content-md-center">
      <div style="min-width: 450px;border: 1px solid darkgray; padding: 50px;">
        <form method="post" action="login" id="form">
          <div class="form-group" style="text-align: -webkit-left;">
            <label for="username">用户名：</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="输入用户名">
          </div>
          <div class="form-group" style="text-align: -webkit-left;">
            <label for="userpwd">密码：</label>
            <input type="password" class="form-control" id="userpwd" name="userpwd" placeholder="输入密码">
          </div>
          <div class="form-group" style="text-align: -webkit-left;">
            <button type="button" class="btn btn-primary" style="width: 100%;" onclick="loginEvent()">登录</button>
          </div>
          <!-- 登录前端提示信息 -->
          <div class="form-group" style="text-align: -webkit-left;">
            <span style="color: red" id="messageTip"></span>
          </div>
          <!-- 登录后端校验提示信息 -->
          <div class="form-group" style="text-align: -webkit-left;">
            <span style="color: red" id="messageServerTip">
              <%= request.getAttribute("serverTip") == null ? "" : request.getAttribute("serverTip") %>
            </span>
          </div>
        </form>
        <button type="button" style="float: left;" class="btn btn-link" data-toggle="modal"
                data-target="#exampleModalCenter">
          注册
        </button>
      </div>
    </div>
  </div>
</div>

<!-- 注册模态框 -->
<div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalCenterTitle">注册</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <label for="rusername" class="col-form-label">用户名:</label>
            <input type="text" class="form-control" id="rusername">
          </div>
          <div class="form-group">
            <label class="col-form-label">性别:</label>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="rsex" id="inlineCheckbox1" value="male" checked>
              <label class="form-check-label" for="inlineCheckbox1">男</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="rsex" id="inlineCheckbox2" value="female">
              <label class="form-check-label" for="inlineCheckbox2">女</label>
            </div>
          </div>
          <div class="form-group">
            <label for="newpwd" class="col-form-label">密码:</label>
            <input type="password" class="form-control" id="newpwd">
          </div>
          <div class="form-group">
            <label for="conpwd" class="col-form-label">确认密码:</label>
            <input type="password" class="form-control" id="conpwd">
          </div>
          <!-- 注册错误提示信息 -->
          <div class="form-group" style="text-align: -webkit-left;">
            <span style="color: red" id="rmessageTip"></span>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="registerEvent()">确认</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>