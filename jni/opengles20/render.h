/**
 * 测试函数接口
 */
#ifdef __cplusplus
extern "C" {
#endif

void gl_initialize(int width,int height);

void gl_view_changed(int width,int height);

void gl_drawFrame();

void gl_uninitialize();

//设置图像数据
void gl_set_framebuffer(const unsigned char* dataY,const unsigned char* dataU,
		const unsigned char* dataV,int width,int height);

/**
 * 画屏
 */
void gl_render_frame(const unsigned char* dataY,const unsigned char* dataU,
		const unsigned char* dataV,int width,int height);


#ifdef __cplusplus
}
#endif
