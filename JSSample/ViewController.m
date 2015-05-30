//
//  ViewController.m
//  JSSample
//
//  Created by 冯 鸿杰 on 14-3-18.
//  Copyright (c) 2014年 掌淘科技. All rights reserved.
//

#import "ViewController.h"
#import "ShareSDKJSBridge.h"
#import <AVFoundation/AVFoundation.h>

@interface ViewController ()
@property (retain) AVAudioPlayer *player;
@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
   
    UIWebView *webView = [[UIWebView alloc] initWithFrame:self.view.bounds];
    webView.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"Sample" ofType:@"html"];
    NSURL *htmlURL = [NSURL fileURLWithPath:path];
    htmlURL = [NSURL URLWithString:@"http://192.168.31.109:8080/cf/index.html"];
    
    webView.delegate = self;
    [self.view addSubview:webView];
    [webView loadRequest:[NSURLRequest requestWithURL:htmlURL]];
     [webView release];
    
  //  [self prepAudio:@"close"];
   // [self prepAudio:@"money"];
  //  [self prepAudio];
  //  [self prepAudio];
   // [self.player play];
    AudioServicesPlaySystemSound (kSystemSoundID_Vibrate);
}

- (BOOL) prepAudio: (NSString *)audioName
{
    NSError *error;
    NSString *path = [[NSBundle mainBundle] pathForResource:audioName ofType:@"wav"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) return NO;
    self.player = [[AVAudioPlayer alloc] initWithContentsOfURL:[NSURL fileURLWithPath:path] error:&error];
    if (!self.player)
    {
        NSLog(@"Error: %@", [error localizedDescription]);
        return NO;
    }
    [self.player prepareToPlay];
   // [self.player setNumberOfLoops:10];
    [self.player play];
    return YES;
}
#pragma mark - UIWebViewDelegate

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    return ![[ShareSDKJSBridge sharedBridge] captureRequest:request webView:webView];
}

@end
