enum AuthorizeStatus { initial, succeeded, failed, canceled, pending }

abstract class AuthorizeResult {
  final AuthorizeStatus status;

  AuthorizeResult(this.status);
}

class AuthorizeSucceeded extends AuthorizeResult {
  final String authCode;

  AuthorizeSucceeded({required this.authCode})
      : super(AuthorizeStatus.succeeded);
}

class AuthorizeFailed extends AuthorizeResult {
  final String code;
  final String? message;

  AuthorizeFailed({required this.code, this.message})
      : super(AuthorizeStatus.failed);
}

class AuthorizePending extends AuthorizeResult {
  AuthorizePending() : super(AuthorizeStatus.pending);
}

class AuthorizeCanceled extends AuthorizeResult {
  AuthorizeCanceled() : super(AuthorizeStatus.canceled);
}
