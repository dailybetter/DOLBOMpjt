import PersonAddIcon from '@mui/icons-material/PersonAdd';
import Autocomplete from '@mui/material/Autocomplete';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import React from 'react';
import { useEffect, useState } from 'react';

import { axiosService } from '../../api/instance';
import { useFetchData } from '../../hooks/useFetchData';
import { ChildModal } from './ChildModal';

const style = {
  position: 'absolute' as const,
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 500,
  height: 430,
  bgcolor: 'background.paper',
  // border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};
interface EventModalProps {
  open: boolean;
  handleClose: () => void;
  eventInfos: any;
  clickInfos: any;
  isEditCard: boolean;
  update: () => void;
}
export const EventModal = ({
  open,
  handleClose,
  eventInfos,
  isEditCard,
  update,
}: EventModalProps) => {
  //기존 내담자 리스트
  const [clientId, setClientId] = useState('');
  const [list, setList] = useState(['김싸피']);
  //선정된 내담자
  const [client, setClient] = useState('');
  //예약 내용
  const [content, setContent] = useState('');
  //시작시간
  const [startTime, setStartTime] = useState('10:00');
  //종료 시간
  const [endTime, setEndTime] = useState('13:00');
  const [toggle, setToggle] = useState(false);
  const listUpdate = () => {
    setToggle(!toggle);
  };
  // autocomplete 목록 불러오기
  useEffect(() => {
    useFetchData().then((res) => setList(res));
  }, [toggle]);
  //isEdit => true 일때 정보 불러오기
  useEffect(() => {
    if (isEditCard) {
      const scheduleId = eventInfos?.event.extendedProps.scheduleId;
      axiosService
        .get(`/schedule/${scheduleId}`)
        .then((res) => {
          setClientId(res.data.clientId);
          setClient(res.data?.title);
          setContent(res.data?.content);
          const startString: any = new Date(res.data?.start).toString();
          const startmid = startString.indexOf(':');
          const endString: any = new Date(res.data?.end).toString();
          const endtmid: number = startString.indexOf(':');
          setStartTime(startString.substring(startmid - 2, startmid + 3));
          setEndTime(endString.substring(endtmid - 2, endtmid + 3));
        })
        .catch((e) => console.log(e));
    }
  }, [eventInfos]);
  useEffect(() => {
    axiosService.get('/client/').then((res) => {
      setClientId(res.data);
    });
  }, []);
  //evet 추가 함수
  const addEvent = async () => {
    const calendarApi = eventInfos.view.calendar;
    const utcStartTime = new Date(`${eventInfos.startStr} ${startTime}`).toISOString();
    const utcEndTime = new Date(`${eventInfos.startStr} ${endTime}`).toISOString();
    const data = {
      clientId,
      title: client == null ? '제목없음' : client,
      start: utcStartTime,
      end: utcEndTime,
      content,
    };
    calendarApi.addEvent(data);
    axiosService.post('/schedule/', data).then((res) => {
      console.log(res);
      update();
    });
    setClient('');
    setContent('');
    handleClose();
  };
  const editEvent = () => {
    const calendarApi = eventInfos.view.calendar;
    const currentEvent = calendarApi.getEventById(eventInfos.event.id);
    const data = {
      scheduleId: currentEvent.extendedProps.scheduleId,
      clientId,
      title: client == null ? '제목없음' : client,
      start: new Date(
        `${eventInfos.event.startStr.substr(0, 10)} ${startTime}`,
      ).toISOString(),
      end: new Date(
        `${eventInfos.event.startStr.substr(0, 10)} ${endTime}`,
      ).toISOString(),
      content,
    };
    axiosService.put('/schedule/', data).then((res) => console.log(res));
    if (currentEvent) {
      currentEvent.setProp('title', client);
      currentEvent.setExtendedProp('title', data.title);
      currentEvent.setExtendedProp('content', data.content);
      currentEvent.setDates(data.start, data.end);
    }
    setClient('');
    setContent('');
    handleClose();
  };
  const removeEvent = () => {
    eventInfos.event.remove();
    const scheduleId = eventInfos?.event.extendedProps.scheduleId;
    axiosService.delete(`/schedule/${scheduleId}`);
    setClient('');
    setContent('');
    handleClose();
  };
  const onCloseandReset = () => {
    setClient('');
    setContent('');
    handleClose();
  };
  // Child Modal 관련 메서드
  // Child Modal 관련 메서드
  // Child Modal 관련 메서드
  const [childOpen, setChildOpen] = React.useState(false);
  const handleChildOpen = () => {
    setChildOpen(true);
  };
  const handleChildClose = () => {
    setChildOpen(false);
  };

  return (
    <>
      <ChildModal
        childOpen={childOpen}
        handleChildClose={handleChildClose}
        listUpdate={listUpdate}
      />
      <Modal open={open} onClose={onCloseandReset}>
        <Box sx={style}>
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              flexDirection: 'row-reverse',
            }}
          >
            <PersonAddIcon className="cursor-pointer" onClick={handleChildOpen} />
            <Typography id="modal-modal-title" variant="h6" component="h2">
              Modal
            </Typography>
          </Box>
          <Autocomplete
            options={list}
            // freeSolo={true}
            value={client ? client : ''}
            onInputChange={(e, value: string) => {
              setClient(value);
              axiosService
                .get(`/client/name/${value}`)
                .then((res) => setClientId(res.data[0].id));
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                label="예약자 찾기"
                sx={{
                  width: 'transparent',
                  margin: '15px auto',
                }}
              />
            )}
          />
          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <TextField
              label="상담 시작 시간"
              type="time"
              value={startTime}
              InputLabelProps={{
                shrink: true,
              }}
              inputProps={{
                step: 300, // 5 min
              }}
              sx={{ width: 200 }}
              onChange={(e) => {
                setStartTime(e.target.value);
              }}
              // eventInfos.startStr
            />
            <TextField
              label="상담 종료 시간"
              type="time"
              value={endTime}
              InputLabelProps={{
                shrink: true,
              }}
              inputProps={{
                step: 300, // 5 min
              }}
              sx={{ width: 200 }}
              onChange={(e) => {
                setEndTime(e.target.value);
              }}
            />
          </Box>
          <TextField
            label="메모"
            multiline
            value={content}
            rows={4}
            fullWidth
            sx={{
              margin: '15px auto',
            }}
            onChange={(e) => setContent(e.target.value)}
          />
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              flexDirection: 'row-reverse',
            }}
          >
            <Button onClick={isEditCard ? editEvent : addEvent} variant="contained">
              {isEditCard ? '수정하기' : '등록하기'}
            </Button>
            {isEditCard && (
              <Button onClick={removeEvent} variant="contained" color="error">
                이벤트 삭제
              </Button>
            )}
          </Box>
        </Box>
      </Modal>
    </>
  );
};
