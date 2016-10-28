uniform mat4 u_MVPMatrix; //总变换矩阵
attribute vec3 a_Position;  //顶点位置

varying vec3 vPosition;//用于传递给片元着色器的顶点位置

void main()
{
   //根据总变换矩阵计算此次绘制此顶点位置
   gl_Position = u_MVPMatrix * vec4(a_Position,1);
   //将顶点的位置传给片元着色器
   vPosition = a_Position;//将原始顶点位置传递给片元着色器
}