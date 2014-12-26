#include<stdio.h>   
#include<stdlib.h>   
#include<string.h>   
#include<errno.h>   
#include<arpa/inet.h>   
#include<sys/socket.h>   
#include<fcntl.h>   
#include<signal.h>   
#include<unistd.h>   

int main() {

    struct sockaddr_in sockserv, sockclient;
    int socketfd, clientfd;
    socklen_t clientsocklen;

    int filefd;
    int cnt;
    int finalcnt = 0;
    int chancecnt;

    char buff[BUFSIZ], buff2[BUFSIZ];

    signal(SIGPIPE, SIG_IGN);

    printf("Signal handler for SIGPIPE over ridden : %s\n", strerror(errno));
    socketfd = socket(AF_INET, SOCK_STREAM, 0);
    printf("Socket Creation: %s\n", strerror(errno));

    filefd = open("/home/ashwin/Desktop/gravity.mp4", O_RDONLY);
    printf("File open: %s\n", strerror(errno));

    sockserv.sin_family = AF_INET;
    sockserv.sin_addr.s_addr = INADDR_ANY;
    sockserv.sin_port = htons(9000);

    bind(socketfd, (struct sockaddr *) &sockserv, sizeof (sockserv));
    printf("Socket Bind: %s\n", strerror(errno));
    listen(socketfd, 10);
    printf("Socket Listen: %s\n", strerror(errno));
    clientfd = accept(socketfd, (struct sockaddr*) &sockclient, &clientsocklen);

    while (1) {
        cnt = read(filefd, buff, 1025);
        printf("Read %d bytes 1 from the file : %s\n", cnt, strerror(errno));
        if (cnt <= 0)
            break;

        finalcnt = send(clientfd, buff, cnt, 0);
        printf("Data written 1 = %d bytes: %s\n", finalcnt, strerror(errno));
        if (finalcnt <= 0) {
            memcpy(buff2, buff, cnt);
            finalcnt = cnt;
            close(clientfd);
            printf("%s\n", strerror(errno));
            break;
        }

    }
    close(clientfd);
    close(socketfd);
    return 0;
} 
