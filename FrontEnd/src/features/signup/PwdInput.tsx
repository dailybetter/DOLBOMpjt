import TextField from '@mui/material/TextField';
import propTypes from 'prop-types';
import { useState } from 'react';

const PwdInput = ({
  setPwdValidate,
  setUserPassword,
}: {
  setPwdValidate: React.Dispatch<React.SetStateAction<boolean>>;
  setUserPassword: React.Dispatch<React.SetStateAction<string>>;
}) => {
  const [userPwd, setUserPwd] = useState('');
  const [confirmPwd, setConfirmPwd] = useState('');
  const [, setPwdEmpty] = useState(true);
  const [pwdError, setPwdError] = useState(false);
  const [pwdLength, setPwdLength] = useState(0);
  //에러 메시지
  const [errorMessage, setErrorMessage] = useState('');
  const ErrorMessageChange = (e: string) => {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,15}$/;
    if (pwdLength < 6 || pwdLength > 15) {
      setErrorMessage('6자 이상  15자 미만으로 입력해주세요');
      setPwdError(true);
    } else if (!passwordRegex.test(e)) {
      setErrorMessage('숫자+영문자+특수문자 조합으로 입력해주세요');
      setPwdError(true);
    } else {
      setErrorMessage('사용가능한 비밀번호 입니다.');
      setPwdError(false);
      setUserPwd(e);
    }
  };
  const pwdvalidateCheck = (e: string) => {
    if (userPwd === e) {
      setPwdValidate(true);
      setUserPassword(e);
    }
  };
  return (
    <>
      <TextField
        error={pwdError}
        margin="dense"
        id={'pwd'}
        label="비밀번호*"
        type="password"
        helperText={errorMessage}
        fullWidth
        onChange={(e) => {
          setPwdLength(e.target.value.length);
          setPwdEmpty(!!e.target.value.length);
          ErrorMessageChange(e.target.value);
        }}
        inputProps={{ maxLength: 16 }}
      />
      <TextField
        error={userPwd !== confirmPwd}
        margin="none"
        id={'pwdConfirm'}
        label="비밀번호 확인*"
        type="password"
        helperText=""
        fullWidth
        onChange={(e) => {
          setConfirmPwd(e.target.value);
          pwdvalidateCheck(e.target.value);
        }}
        inputProps={{ maxLength: 16 }}
      />
    </>
  );
};

PwdInput.propTypes = {
  setPwdValidate: propTypes.func,
  setUserPassword: propTypes.func,
};

export default PwdInput;
