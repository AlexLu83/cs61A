; Lab 13: Final Review

; Q3
(define (compose-all funcs)
  (define (helper x func)
          (if (null? func)
               x
               (helper ((car func) x) (cdr func))
          )
  )
  (lambda (s) (helper s funcs))
)
