# two
#include <iostream>
#include <string>
#include <string.h>
#include<queue>
using namespace std;
#define MAX_CH 2
#define GET_IDX(c) ((c) == 'D' ? 0 : 1)
const int MAX_NODE = 300;
#define mod(x) ((x) % 1000000007)
int nxt[MAX_NODE][MAX_CH],tail[MAX_NODE],f[MAX_NODE],size,bit_tag[MAX_NODE];
int m,n;
#define BIT_L (2)
#define MAX_L 101
int dp[MAX_L][MAX_L][MAX_NODE][(1<<BIT_L)];
void init(){
    size = 1;
    memset(dp, 0, sizeof(dp));
    memset(nxt[0], 0, sizeof(nxt[0]));
    memset(tail, 0, sizeof(tail));
    memset(f, 0, sizeof(f));
    memset(bit_tag, 0, sizeof(bit_tag));
}
int newnode(){ 
    memset(nxt[size],0,sizeof(nxt[size]));
    f[size] = tail[size] = 0;
    bit_tag[size] = 0;
    return size++;
}
void insert(string s, int bit){
    int p = 0;
    for(size_t i = 0; i < s.size(); i++){
        int& x = nxt[p][GET_IDX(s[i])];
        p = x ? x : (x = newnode());
    }
    tail[p]++;
    bit_tag[p] = bit;
}
void makenxt(){
    queue<int>q;
    f[0] = 0;
    for(int i = 0; i < MAX_CH; i++){
        int v = nxt[0][i];
        if(!v) continue;
        f[v]=0;
        q.push(v);
    }
    while(!q.empty()){
        int u = q.front();
        q.pop();
        for(int i = 0; i < MAX_CH; i++){
            int v = nxt[u][i];
            if(v){
                q.push(v);
                f[v] = nxt[f[u]][i];
            }
            else nxt[u][i] = nxt[f[u]][i];
        }
    }
}
int ac_next(char c, int v, int& flag){
        flag = 0;
        while(v && !nxt[v][GET_IDX(c)]) v = f[v];
        v = nxt[v][GET_IDX(c)];
        int tmp = v;
        while(tmp){
            flag |= bit_tag[tmp];
            tmp=f[tmp];
        }
        return v;
}
void DP(){
    int flag = 0, nxt_st = 0;
    dp[0][0][0][0] = 1;
    for(int i = 0; i<= m; i++){
        for(int j = 0; j <= n; j++){
            for(int k = 0; k < size; k++){
                for(int l = 0; l < (1 << BIT_L); l++){
                    if(i > 0){ // i-i j  //D
                        nxt_st = ac_next('D', k, flag);
                        dp[i][j][nxt_st][l | flag] += dp[i-1][j][k][l];
                        dp[i][j][nxt_st][l | flag] = mod(dp[i][j][nxt_st][l|flag]);
                    }
                    if(j > 0){// i j-1 R
                        nxt_st = ac_next('R', k, flag);
                        dp[i][j][nxt_st][l | flag] += dp[i][j-1][k][l];
                        dp[i][j][nxt_st][l | flag] = mod(dp[i][j][nxt_st][l|flag]);
                    }
                }
            }
        }
    }
}
int main(){
    int T;
    string str;
    cin >> T;
    while(T--){
        init();
        cin >> n >> m;
        for(int i = 0; i < BIT_L; i++){
            cin >> str;
            insert(str, 1 << i);
        }
        makenxt();
        DP();
        int ans = 0;
        for(int i = 0; i < size; i++){
            ans += dp[m][n][i][(1 << BIT_L) -1];
            ans = mod(ans);
        }
        cout << ans <<endl;
    }
    return 0;
}
