


(define-macro (def func bindings body)
    `(define ,func (lambda ,bindings ,body))
)
; def f (x y) (+ x y)
