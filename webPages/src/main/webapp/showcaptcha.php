require_once("recaptchalib.php");
$publickey = "[6LctGhATAAAAAOP1vf6XhLbCiCryO96AUEmhQhM4]";
// show the captcha
echo recaptcha_get_html($publickey);