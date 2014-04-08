//
//  AppDelegate.h
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FFEF/FatFractal.h>

/**
 * @mainpage  <b>The hoodyoodoo tutorial Module 1.</b> 
 *
 *            The hoodyoodoo tutorial exercises the FatFractal backend for authentication
 * and the four basic CRUD functions:<br>
 *            Create<br>
 *            Retrieve<br> 
 *            Update<br> 
 *            Delete<br>  
 *            <p/> <br/>
 */

/*! \brief The AppDelegate class */

/*!
 * The AppDelegate interface that will hold a sender and selector for callback functionality 
 * for the common authentication service.
 @attribute id _sender is the instance of the calling class
 @attribute SEL _action is the callback selector from the action.
 */
@interface AppDelegate : UIResponder <UIApplicationDelegate> {
    id _sender; /*!< The instance id for the calling class used for #showLoginWithDelegate:action:message:*/
    SEL _action; /*!< The callback selector from the calling class used for #showLoginWithDelegate:action:message:*/
}

/** 
 The static method for the FatFractal class used for this tutorial.
 */
+ (FatFractal *) ff;

/** UIWindow property */
@property (strong, nonatomic) UIWindow *window;

/** UINavigationController property */
@property (strong, nonatomic) UINavigationController *navigationController;

/** 
 * The #showLoginWithDelegate:action:message: method creates a common authentication service for 
 * your application with a callback to allow other classes to use it whenever they want within 
 * the application flow.
 */
- (void)showLoginWithDelegate:(id)sender action:(SEL)action message:(NSString *)message;

@end
