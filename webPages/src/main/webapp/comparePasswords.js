/**
 * Created by jhark on 11/23/2015.
 */
function comparePasswords(enteredPassword, password)
{
    password = replaceAll("_", "/", password);
    password = CryptoJS.AES.decrypt(password, enteredPassword).toString(CryptoJS.enc.Latin1);
    if (enteredPassword == password)
    {
        return "Confirmed";
    }
    else
    {
        return "Unconfirmed";
    }
}
function replaceAll(find, replace, str) {
    return str.replace(new RegExp(find, 'g'), replace);
}

