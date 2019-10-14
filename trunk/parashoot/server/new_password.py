import urllib2
import smtplib

TXT_FILE = "current_password.txt"

GET_RANDOM_WORD_ADDRESS = 'http://setgetgo.com/randomword/get.php'

MAIL_PORT = 587

PASSWORD = 'fatcow18'

GMAIL_DOMAIN = 'smtp.gmail.com'

MESSAGE_CONTENT = 'The server has failed to change password: '

MAIL = 'gadw17@gmail.com'


def send_mail(reason):
    try:
        smtp_obj = smtplib.SMTP(GMAIL_DOMAIN, MAIL_PORT)
        smtp_obj.starttls()
        smtp_obj.login(MAIL, PASSWORD)
        smtp_obj.sendmail(MAIL, MAIL, MESSAGE_CONTENT + reason)
        smtp_obj.quit()
        print 'Mail was sent successfully'
    except smtplib.SMTPException:
        print 'Failed sending mail!'


def main():
    try:
        response = urllib2.urlopen(GET_RANDOM_WORD_ADDRESS)
        result = response.read()
        text_file = open(TXT_FILE, "w")
        text_file.write(result)
        text_file.close()
        print result
    except urllib2.URLError as e:
        reason = e.reason
        print reason
        send_mail(reason)


if __name__ == "__main__":
    main()
