import processing.core.PApplet;
import processing.core.PVector;
import java.util.Random;
import java.util.ArrayList;

public class Main extends PApplet {
    int WIDTH = 800;
    int HEIGHT = 800;
    int RADIUS = 10;

    boolean upPressed = false;
    boolean downPressed = false;
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean paused = false;
    boolean drawMode = false;
    boolean turretMode = false;

    ArrayList<bullet> BulletList = new ArrayList<>();
    ArrayList<Wall> Obstacles = new ArrayList<>();
    ArrayList<Turret> Turrets = new ArrayList<>();

    player Player;
    Random rand = new Random();


    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(WIDTH, HEIGHT); // Example size, customize as needed
    }

    public void setup() {
        // Initialization code goes here
//        fill(0);
        Player = new player(400,400);
    }

    public void draw() {
        if(!paused){
            background(255);
            Player.velocity = new PVector(0, 0); // Reset velocity each frame

            if (upPressed) {
                Player.velocity.add(new PVector(0, -1));
            }
            if (downPressed) {
                Player.velocity.add(new PVector(0, 1));
            }
            if (leftPressed) {
                Player.velocity.add(new PVector(-1, 0));
            }
            if (rightPressed) {
                Player.velocity.add(new PVector(1, 0));
            }

            Player.velocity.setMag(3); // Set speed (optional)

            Player.move();
            Player.display();
            for (int i = 0; i < BulletList.size(); i++) {
                bullet shot = BulletList.get(i);
                shot.move();
                shot.display();
            }
            for(int i = 0; i<Obstacles.size();i++){
                push();
                strokeWeight(15);
                Obstacles.get(i).display();
                pop();
            }
            for(int i = 0; i<Turrets.size();i++){
                push();
                fill(0);
                Turrets.get(i).display();
                pop();
            }

        }else{
            background(255);
            Player.display();
            for(int i = 0; i<Obstacles.size();i++){
                push();
                strokeWeight(15);
                Obstacles.get(i).display();
                pop();
            }
            for(int i = 0; i<Turrets.size();i++){
                push();
                fill(0);
                Turrets.get(i).display();
                pop();
            }
            for (int i = 0; i < BulletList.size(); i++) {
                bullet shot = BulletList.get(i);
                shot.display();
            }
            rectMode(CENTER);
            fill(125, 150);
            rect(400,400,500,400, 20);
            fill(0);
            textSize(75f);
            textAlign(CENTER, CENTER);
            text("GAME PAUSED", (float)WIDTH/2, (float)HEIGHT/3);
            fill(0,255,0);
            rect(275, 500, 150, 50, 20);
            fill(0);
            textSize(25f);
            text("Draw Mode", 275, 500);
            fill(255,0,0);
            rect(525, 500, 150, 50, 20);
            fill(0);
            textSize(25f);
            text("Shop", 525, 500);
            fill(255);
            if(drawMode){
                background(255);
                Player.display();
                for (int i = 0; i < BulletList.size(); i++) {
                    bullet shot = BulletList.get(i);
                    shot.display();
                }

                fill(0,255,0);
                rect(100, 50, 150, 50, 20);
                fill(0);
                textSize(25);
                text("Draw Mode", 100,50);

                fill(0,255,0);
                rect(100, 90, 125, 45, 20);
                fill(0);
                textSize(20);
                if(turretMode == false) {
                    text("Turret Mode", 100, 90);
                }else{
                    text("Wall Mode", 100, 90);
                }

                fill(255,0,0);
                rect(700, 50, 150, 50, 20);
                fill(0);
                textSize(25);
                text("Done", 700,50);

                for(int i = 0; i<Obstacles.size();i++){
                    push();
                    strokeWeight(15);
                    Obstacles.get(i).display();
                    pop();
                }
                for(int i = 0; i<Turrets.size();i++){
                    push();
                    fill(0);
                    Turrets.get(i).display();
                    pop();
                }

                fill(255);
            }
        }
        // Drawing code (executed repeatedly)
    }

    public void mousePressed(){
        if(!paused) {
            Player.shoot();
        }else{
            if((mouseX >= 275 - 150/2 && mouseX <= 275 + 150/2 && mouseY >= 500 - 50/2 && mouseY <= 500 + 50/2) && !drawMode){
                drawMode = true;
            }
            if (drawMode && mouseX >= 700 - 150/2 && mouseX <= 700 + 150/2 && mouseY >= 50 - 50/2 && mouseY <= 50 + 50/2) {
                drawMode = false;
            }
            if (drawMode && mouseX >= 100 - 125/2 && mouseX <= 100 + 125/2 && mouseY >= 90 - 45/2 && mouseY <= 90 + 45/2) {
                turretMode = !turretMode;
            }
        }
    }

    int counter = 0;
    ArrayList<Integer> coords = new ArrayList<>();
    public void mouseClicked(){
        if(drawMode && !turretMode){
            if((mouseX < 700 - 150/2 || mouseX > 700 + 150/2 || mouseY < 50 - 50/2 || mouseY > 50 + 50/2)&&
                    (mouseX < 100 - 125/2 || mouseX > 100 + 125/2 || mouseY < 90 - 45/2 || mouseY > 90 + 45/2)){
                counter += 1;
                coords.add(mouseX);
                coords.add(mouseY);
                if (counter == 2) {
                    fill(0);
                    Obstacles.add(new Wall(coords.get(0), coords.get(1), coords.get(2), coords.get(3)));
                    counter = 0;
                    coords.clear();
                }
            }
        }
        if(drawMode && turretMode){
            if((mouseX < 700 - 150/2 || mouseX > 700 + 150/2 || mouseY < 50 - 50/2 || mouseY > 50 + 50/2)&&
                    (mouseX < 100 - 125/2 || mouseX > 100 + 125/2 || mouseY < 90 - 45/2 || mouseY > 90 + 45/2)){
                Turret turret = new Turret(mouseX, mouseY);
                Turrets.add(turret);
            }
        }
    }

    public ArrayList<PVector> getLinePoints(int x0, int y0, int x1, int y1) {
        ArrayList<PVector> points = new ArrayList<>();
        int dx = abs(x1 - x0);
        int dy = abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            points.add(new PVector(x0, y0));
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return points;
    }

    public void keyPressed(){
        if(!paused) {
            if (key == 'w') {
                upPressed = true;
            }
            if (key == 's') {
                downPressed = true;
            }
            if (key == 'a') {
                leftPressed = true;
            }
            if (key == 'd') {
                rightPressed = true;
            }
        }
        if (key == ' ') {
            if(paused){
                paused = false;
            }else if(!paused){
                paused = true;
            }
            drawMode = false;
            turretMode = false;
        }
    }

    public void keyReleased(){
        if(key == 'w'){upPressed = false;}
        if(key == 's'){downPressed = false;}
        if(key == 'a'){leftPressed = false;}
        if(key == 'd'){rightPressed = false;}
    }

    public class player{
        PVector velocity;
        PVector position;
        public player(int x,int y){
            velocity = new PVector(0,0);
            position = new PVector(x, y);
        }
        public void move(){
            position.add(velocity);
            if (position.x >= WIDTH - RADIUS) {
                position.x = WIDTH - RADIUS;
            }
            if (position.x <= RADIUS) {
                position.x = RADIUS;
            }
            if (position.y >= HEIGHT - RADIUS) {
                position.y = HEIGHT - RADIUS;
            }
            if (position.y <= RADIUS) {
                position.y = RADIUS;
            }
            for(int z = 0; z < Obstacles.size(); z++){
                ArrayList<PVector> line = getLinePoints(Obstacles.get(z).xPosition1, Obstacles.get(z).yPosition1, Obstacles.get(z).xPosition2, Obstacles.get(z).yPosition2);
                for(int y = 0; y < line.size(); y++){
                    if(position.dist(line.get(y))<=RADIUS+6){
                        position.sub(velocity);
                    }
                }
            }
            for(int z = 0; z < Turrets.size(); z++){
                PVector circPos = new PVector(Turrets.get(z).xPosition, Turrets.get(z).yPosition);
                if(position.dist(circPos)<=15+RADIUS){
                    position.sub(velocity);
                }
            }
        }
        public void display(){
            circle(position.x, position.y, 2*RADIUS);
        }
        public void shoot(){
            PVector mouse = new PVector(mouseX, mouseY);
            PVector direction = PVector.sub(mouse, position); // vector from player to mouse
            bullet shot = new bullet(position, direction);    // direction is passed
            BulletList.add(shot);
        }
    }
    public class Turret{
        int xPosition;
        int yPosition;
        public Turret(int Xpos, int Ypos){
            xPosition = Xpos;
            yPosition = Ypos;
        }
        public void display(){
            circle(xPosition,yPosition, 35);
        }
    }
    public class Wall{
        int xPosition1;
        int yPosition1;
        int xPosition2;
        int yPosition2;
        public Wall(int Xpos1, int Ypos1, int Xpos2, int Ypos2){
            xPosition1 = Xpos1;
            yPosition1 = Ypos1;
            xPosition2 = Xpos2;
            yPosition2 = Ypos2;
        }
        public void display(){
            line(xPosition1,yPosition1,xPosition2,yPosition2);
        }
    }
    public class bullet{
        PVector position;
        PVector velocity;
        public bullet(PVector pos, PVector velo){
            position = pos.copy();
            velocity = velo.copy().setMag(20);
        }
        public void move(){
            position.add(velocity);
            if (position.x >= WIDTH - 2 || position.x <= 2) {
                BulletList.remove(this);
            }
            if (position.y >= HEIGHT - 2 || position.y <= 2) {
                BulletList.remove(this);
            }
            for(int z = 0; z < Obstacles.size(); z++){
                ArrayList<PVector> line = getLinePoints(Obstacles.get(z).xPosition1, Obstacles.get(z).yPosition1, Obstacles.get(z).xPosition2, Obstacles.get(z).yPosition2);
                for(int y = 0; y < line.size(); y++){
                    if(position.dist(line.get(y))<=RADIUS+6){
                        BulletList.remove(this);
                    }
                }
            }
            for(int z = 0; z < Turrets.size(); z++){
                PVector circPos = new PVector(Turrets.get(z).xPosition, Turrets.get(z).yPosition);
                if(position.dist(circPos)<=15+RADIUS){
                    BulletList.remove(this);
                }
            }
        }
        public void display(){
            circle(position.x, position.y, 5);
        }
    }


}