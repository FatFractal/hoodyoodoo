//
//  AppDelegate.m
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "AppDelegate.h"

@implementation AppDelegate

static FatFractal *_ff;

+ (FatFractal *) ff {
    return _ff;
}

@synthesize window = _window, navigationController;


- (void)showLoginWithDelegate:(id)sender action:(SEL)action message:(NSString *)message {
    UIAlertView *prompt = [[UIAlertView alloc] initWithTitle:@"Login/ Register"
                                                     message:message
                                                    delegate:self
                                           cancelButtonTitle:@"Cancel"
                                           otherButtonTitles:@"Enter", nil];
    [prompt setAlertViewStyle:UIAlertViewStyleLoginAndPasswordInput];
    _action = action;
    _sender = sender;
    [prompt show];
}

#pragma clang diagnostic ignored "-Warc-performSelector-leaks"

- (void)alertView:(UIAlertView *)prompt clickedButtonAtIndex:(NSInteger)buttonIndex {
    [self resignFirstResponder];
    if(buttonIndex == 0) {
        return;
    } else {
        NSError *error;
        [_ff loginWithUserName:[[prompt textFieldAtIndex:0] text] andPassword:[[prompt textFieldAtIndex:1] text] error:&error];
        NSLog(@"AppDelegate alertView clickedButtonAtIndex: username: %@", [prompt textFieldAtIndex:0]);
        NSLog(@"AppDelegate alertView clickedButtonAtIndex: password: %@", [prompt textFieldAtIndex:1]);
        if (error) {
            [self showLoginWithDelegate:_sender action:_action
                                message:@"Please Login"];
        } else {
            if([_sender respondsToSelector:_action]) {
                [_sender performSelector:_action];
            } else
                NSLog(@"Callback function cannot be found");
       }
    }
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    NSLog(@"Received push notification %@", userInfo);
    UIAlertView *prompt = [[UIAlertView alloc] initWithTitle:@"Been outdone!"
                                                     message:[userInfo valueForKeyPath:@"aps.alert"]
                                                    delegate:nil
                                           cancelButtonTitle:@"Okie Dokie"
                                           otherButtonTitles:nil];
    [prompt show];
}

- (void)application:(UIApplication *)app didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)devToken {
    NSLog(@"didRegisterForRemoteNotifications, deviceToken is: %@: ", [devToken description]);
    [[FatFractal main] registerNotificationID:[devToken description]];
}

- (void)application:(UIApplication *)app didFailToRegisterForRemoteNotificationsWithError:(NSError *)err {
    NSLog(@"Error in registration. Error: %@", err);
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    /* Override point for customization after application launch. */
    
    /* Set the base url that your application will use */
    _ff = [[FatFractal alloc]
                      initWithBaseUrl:@"http://acme.fatfractal.com/hoodyoodoo"
                      sslUrl:@"https://acme.fatfractal.com/hoodyoodoo"];
    [_ff setDebug:TRUE];
    [FFHttpDelegate addTrustedHost:@"acme.fatfractal.com"];
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    [self.window makeKeyAndVisible];
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     */
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    /*
     Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
     */
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    /*
     Called when the application is about to terminate.
     Save data if appropriate.
     See also applicationDidEnterBackground:
     */
}

@end
