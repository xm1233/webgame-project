import {GameObject} from "@/assets/scripts/GameObject";
import {Wall} from "@/assets/scripts/Wall";
import {Snake} from "@/assets/scripts/Snake";

export class GameMap extends GameObject{
    constructor(ctx,parent,store) {
        super();
        this.ctx=ctx;
        this.parent=parent;
        this.L=0;
        this.rows=13;
        this.cols=14;
        this.store=store
        this.walls=[];
        this.inner_wall_count=20;
        this.snakes=[
            new Snake({id:"0",color:"#4876EC",r:this.rows-2,c:1},this),
            new Snake({id:"1",color:"#F94848",r:1,c:this.cols-2},this),
        ]
    }

    add_listening_events(){

        if(this.store.state.record.is_record){
            let k=0;
            const a_steps=this.store.state.record.a_steps;
            const b_steps=this.store.state.record.b_steps;
            const loser=this.store.state.record.record_loser;
            const [snake0,snake1]=this.snakes;
            const interval_id=setInterval(()=>{
                if(k>=a_steps.length-1){
                    if(loser==="all"||loser==="a"){
                        snake0.status="die";
                    }
                    else if(loser==="all"||loser==="b"){
                        snake1.status="die";
                    }
                    clearInterval(interval_id);
                }
                else{
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            },300);
        }
        else{
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown",e=>{
                let d=-1;
                if(e.key==='w')d=0;
                else if(e.key==='d')d=1;
                else if(e.key==='s')d=2;
                else if(e.key==='a')d=3;
                if(d>=0){
                    this.store.state.pk.socket.send(JSON.stringify({
                        event:"move",
                        direction:d
                    }))
                }
            });
        }
    }

    // check_connectivity(g,sx,sy,tx,ty){
    //     if(sx===tx&&sy===ty)return true;
    //     g[sx][sy]=true;
    //     let dx=[0,0,-1,1],dy=[-1,1,0,0];
    //     for (let i = 0; i <4; i++) {
    //         let x=sx+dx[i],y=sy+dy[i];
    //         if(!g[x][y]&&this.check_connectivity(g,x,y,tx,ty))return true;
    //     }
    //     return false;
    // }

    check_ready(){ //判断两条蛇是否准备好下一回合
        for(const snake of this.snakes){
            if(snake.status!=="idle")return false;
            if(snake.direction===-1)return false;
        }
        return true;
    }

    // //没有碰到两条蛇的身体和障碍物
    // check_valid(cell){
    //     for(const wall of this.walls){
    //         if(wall.r===cell.r&&wall.c===cell.c)return false;
    //     }
    //     for(const snake of this.snakes){
    //         let k=snake.cells.length;
    //         if(!snake.check_tail_increasing())k--;
    //         for(let i=0;i<k;i++){
    //             if(snake.cells[i].r===cell.r&&snake.cells[i].c===cell.c)return false;
    //         }
    //     }
    //     return true;
    // }


    // create_walls(){
    //     const g=[];
    //     //四周加上障碍物
    //     for (let i = 0; i < this.rows; i++) {
    //         g[i]=[]
    //         for (let j = 0; j < this.cols; j++) {
    //             if(i===0||i===this.rows-1)g[i][j]=true;
    //             else if(j===0||j===this.cols-1)g[i][j]=true;
    //             else g[i][j]=false;
    //         }
    //     }
    //
    //     //加随机障碍物
    //     for (let i = 0; i < this.inner_wall_count/2; i++) {
    //         for(let j=0;j<1000;j++){
    //             let c=parseInt(Math.random()*this.cols);
    //             let r=parseInt(Math.random()*this.rows);
    //             if(g[r][c]||g[this.rows-1-r][this.cols-1-c])continue;
    //             if(r===this.rows-2&&c===1)continue;
    //             if(r===1&&c===this.cols-2)continue;
    //             g[this.rows-1-r][this.cols-1-c]=g[r][c]=true;
    //             break;
    //         }
    //     }
    //     const copy_g=JSON.parse(JSON.stringify(g));
    //     if(!this.check_connectivity(copy_g,this.cols-2,1,1,this.rows-2))return false;
    //
    //     for (let i = 0; i < this.rows; i++) {
    //         for(let j=0;j<this.cols;j++){
    //             if(g[i][j]){
    //                 this.walls.push(new Wall(i,j,this));
    //             }
    //         }
    //     }
    //     return true;
    // }

    create_walls(){
        const g=this.store.state.pk.gamemap;
            for (let i = 0; i < this.rows; i++) {
                for(let j=0;j<this.cols;j++){
                    if(g[i][j]===1){
                        this.walls.push(new Wall(i,j,this));
                    }
                }
            }
            return true;
    }

    start(){
        for (let i = 0; i < 1000; i++) {
            this.create_walls();
        }
        this.add_listening_events();
    }

    update_size(){
        this.L=Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows);
        this.ctx.canvas.width=this.L*this.cols
        this.ctx.canvas.height=this.L*this.rows
    }

    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
        this.render();
    }

    render(){
        // this.ctx.fillStyle='green';
        // this.ctx.fillRect(0,0,this.ctx.canvas.width,this.ctx.canvas.height);
        const color_even="#AAD751",color_odd="#A2D149";
        for(let r=0;r<this.rows;r++){
            for (let c = 0; c < this.cols; c++) {
                if((r+c)%2==0){
                    this.ctx.fillStyle=color_even;
                }
                else{
                    this.ctx.fillStyle=color_odd;
                }
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L)
            }
        }
    }
}

