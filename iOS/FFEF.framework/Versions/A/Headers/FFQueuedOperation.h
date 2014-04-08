//
//  FFQueuedOperation.h
//  FF-IOS-Framework
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

/** \brief The model class for working with queued operations on the the FatFractal Platform. */
/*! The FFQueuedOperation object model includes all of the parameters that can be specified 
 * regarding queued operations with the FatFractal Platform for offline or other performance
 * reasons.
 */
@interface FFQueuedOperation : NSObject

/** The method which was queued. createObj, updateObj, deleteObj, updateBlob, or postObj*/
@property (strong, nonatomic, readonly)     NSString    *queuedMethod;

/** The object to be processed*/
@property (strong, nonatomic, readonly)     id          queuedObj;

/** The collection uri to which this queued operation will be sent */
@property (strong, nonatomic, readonly)     NSString    *queuedUri;

/** The NSData if this is a queued updateBlob */
@property (strong, nonatomic, readonly)     NSData      *queuedBlob;

/** The mime type if this is a queued updateBlob */
@property (strong, nonatomic, readonly)     NSString    *queuedMimeType;

/** The member name (eg imageData) if this is a queued updateBlob */
@property (strong, nonatomic, readonly)     NSString    *queuedMemberName;

/*! Initializer that accepts a queuedMethod, object to be processed and collection Uri to which the object will be sent.
 * @param NSString - the queuedMethod (createObj, updateObj, deleteObj, updateBlob, or postObj).
 * @param id - the object to be processed
 * @param NSString - the collection uri for the object
 * @return <b>id</b> the queued object
 */
- (id) initWithMethod:(NSString *)theMethod obj:(id)theObj uri:(NSString *)theUri;

/*! Initializer that accepts an object with blob data. Parameters include the queuedMethod, object to be processed, the 
 * collection uri to which this queued operation will be sent.
 * @param NSString - the user to be added
 * @param id - the object to be processed
 * @param NSString - the collection uri for the object
 * @param NSData - the NSData if this is a queued updateBlob
 * @param NSString - the member name (eg imageData) if this is a queued updateBlob
 * @return <b>id</b> the queued object
 */
- (id) initWithMethod:(NSString *)theMethod obj:(id)theObj uri:(NSString *)theUri
                 blob:(NSData *)theBlob mimeType:(NSString *)theMimeType memberName:(NSString *)theMemberName;

@end

/*! The FFQueuedOperation extension.
 */
@interface FFQueuedOperation ()

/** The method which was queued. createObj, updateObj, deleteObj, updateBlob, or postObj*/
@property (strong, nonatomic)   NSString    *queuedMethod;

/** The object to be processed*/
@property (strong, nonatomic)   id          queuedObj;

/** The collection uri to which this queued operation will be sent */
@property (strong, nonatomic)   NSString    *queuedUri;

/** The NSData if this is a queued updateBlob */
@property (strong, nonatomic)   NSData      *queuedBlob;

/** The mime type if this is a queued updateBlob */
@property (strong, nonatomic)   NSString    *queuedMimeType;

/** The member name (eg imageData) if this is a queued updateBlob */
@property (strong, nonatomic)   NSString    *queuedMemberName;

@end

