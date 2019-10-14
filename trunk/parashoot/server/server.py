from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import urlparse
from urlparse import parse_qs
from cgi import parse_header, parse_multipart

CURRENT_PASSWORD_FILE = "current_password.txt"

REQ_ID = 'req_id'
REQ_DAILY_GIFT = 'daily_gift'
PARAM_PASS = 'pass'

RESP_VALUE_TRUE = 'true'
RESP_VALUE_FALSE = 'false'


class S(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_GET(self):
        self._set_headers()
        request = self.path
        req_id = urlparse.parse_qs(urlparse.urlparse(request).query).get(REQ_ID)
        print urlparse.parse_qs(urlparse.urlparse(request).query)
        if req_id == ['daily_gift']:
            self.wfile.write(RESP_VALUE_TRUE)

    def do_HEAD(self):
        self._set_headers()

    def parse_POST(self):
        ctype, pdict = parse_header(self.headers['content-type'])
        if ctype == 'multipart/form-data':
            postvars = parse_multipart(self.rfile, pdict)
        elif ctype == 'application/x-www-form-urlencoded':
            length = int(self.headers['content-length'])
            postvars = parse_qs(self.rfile.read(length), keep_blank_values=1)
        else:
            postvars = {}
        return postvars

    def do_POST(self):
        self._set_headers()
        post_vars = self.parse_POST()
        req_id = post_vars.get(REQ_ID)
        if req_id == [REQ_DAILY_GIFT]:
            pass_param = post_vars.get(PARAM_PASS)
            text_file = open(CURRENT_PASSWORD_FILE, "r")
            password = text_file.readline()
            print "Is " + str(pass_param[0]) + " is " + str(password)
            if pass_param == [password]:
                self.wfile.write(RESP_VALUE_TRUE)
            else:
                self.wfile.write(RESP_VALUE_FALSE)
            text_file.close()


def run(server_class=HTTPServer, handler_class=S, port=80):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print 'Starting http...'
    httpd.serve_forever()


if __name__ == "__main__":
    from sys import argv

    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()
