function post() {
    window.location.href = "post.html";
}

/**
 * 判断输入的字符串是否为空（正常的取值）
 * @param str
 * @returns {boolean}
 */
function isEmpty(str){
    var flag = false;
    if(str == null || str == undefined || str.trim() == ""){
        flag = true;
    }
    return flag;
}

/**
 * 字符串的校验，4-16位， 包含字母数字下划线，以字母开头
 * @param str
 * @returns {boolean}
 */
function checkName(str){
    //1.用户名：用户名由英文字母和数字组成的4-16位字符，以字母开头
    var reg = /^[a-zA-Z][a-zA-Z0-9_]{3,17}$/;
    var flag = reg.test(str);
    return flag;
}

